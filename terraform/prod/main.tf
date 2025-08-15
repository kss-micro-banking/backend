terraform {
  required_version = ">= 0.1.1"

  backend "azurerm" {
    resource_group_name  = "main-rg"
    storage_account_name = "kutal"
    container_name       = "tfstate"
    key                  = "kss/backend/prod.tfstate"
  }
}

module "app" {
  source = "../module/app"

  env              = var.env
  container_image  = var.container_image
  container_cpu    = var.container_cpu
  container_memory = var.container_memory
}
