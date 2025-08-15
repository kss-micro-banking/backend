
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

