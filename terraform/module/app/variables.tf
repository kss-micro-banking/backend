variable "app_name" {
  type    = string
  default = "kss"
}

variable "env" {
  type = string
}

variable "location" {
  type    = string
  default = "UK South"
}

variable "app_port" {
  type    = number
  default = 8080
}
variable "container_cpu" {
  type = string
}

variable "container_memory" {
  type = string
}

variable "container_image" {
  type = string
}

variable "jwt_secret" {
  type = string
}

variable "db_url" {
  type = string
}

variable "mail_user" {
  type = string
}

variable "mail_password" {
  type = string
}

variable "client_url" {
  type = string
}
