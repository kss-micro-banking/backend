variable "env" {
  type    = string
  default = "dev"
}

variable "container_cpu" {
  type    = number
  default = 0.5
}

variable "container_memory" {
  type    = string
  default = "1Gi"
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
