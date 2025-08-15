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
  db_url           = var.db_url
  mail_user        = var.mail_user
  mail_password    = var.mail_password
  jwt_secret       = var.jwt_secret
  client_url       = var.client_url
}
