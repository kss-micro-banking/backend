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

