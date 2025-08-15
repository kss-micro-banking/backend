terraform {
  required_version = ">= 0.1.1"
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~>4.0"
    }
  }

}

data "azurerm_container_registry" "main" {
  resource_group_name = "main-rg"
  name                = "kutal"
}

resource "azurerm_resource_group" "main" {
  name     = "${var.app_name}-${var.env}-rg"
  location = var.location
}

resource "azurerm_container_app_environment" "main" {
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  name                = "${var.app_name}-${var.env}-cae"
}

resource "azurerm_log_analytics_workspace" "main" {
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  name                = "${var.app_name}-${var.env}-law"
}

resource "azurerm_user_assigned_identity" "main" {
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  name                = "${var.app_name}-${app.env}-user-id"
}

resource "azurerm_role_assignment" "acrPull" {
  role_definition_name = "AcrPull"
  scope                = data.azurerm_container_registry.main.id
  principal_id         = azurerm_user_assigned_identity.main.principal_id
}

resource "azurerm_container_app" "main" {
  resource_group_name          = azurerm_resource_group.main.name
  name                         = "${var.app_name}-${var.env}"
  container_app_environment_id = azurerm_container_app_environment.main.id
  revision_mode                = "Single"

  identity {
    type         = "UserAssigned"
    identity_ids = [azurerm_user_assigned_identity.main.id]

  }

  registry {
    identity = azurerm_user_assigned_identity.main.id
    server   = data.azurerm_container_registry.main.login_server
  }

  ingress {
    external_enabled = true
    target_port      = var.app_port
    traffic_weight {
      percentage      = 100
      latest_revision = true
    }

  }

  template {
    container {
      name   = var.app_name
      image  = var.container_image
      cpu    = var.container_cpu
      memory = var.container_memory
    }
  }

  depends_on = [
    azurerm_role_assignment.acrPull,
    azurerm_user_assigned_identity.main
  ]
}
