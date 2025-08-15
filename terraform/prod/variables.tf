variable "env" {
  type    = string
  default = "prod"
}

variable "container_cpu" {
  type    = number
  default = 1
}

variable "container_memory" {
  type    = string
  default = "2Gi"
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
