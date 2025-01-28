provider "google" {
  project = var.project_id
  region  = var.region
}

module "example_module" {
  source     = "./modules/example-module"
  instance_type = var.instance_type
  region        = var.region
}

output "instance_ip" {
  value = module.example_module.instance_ip
}