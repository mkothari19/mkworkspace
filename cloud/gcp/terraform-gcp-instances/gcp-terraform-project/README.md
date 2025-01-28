# GCP Terraform Project

This project is designed to provision resources on Google Cloud Platform (GCP) using Terraform. It includes a modular structure to facilitate the management of GCP resources.

## Project Structure

```
gcp-terraform-project
├── modules
│   └── example-module
│       ├── main.tf
│       ├── outputs.tf
│       ├── variables.tf
├── main.tf
├── outputs.tf
├── variables.tf
└── README.md
```

## Prerequisites

- Terraform installed on your local machine.
- A Google Cloud Platform account.
- Google Cloud SDK installed and configured.

## Setup Instructions

1. **Clone the repository**:
   ```
   git clone <repository-url>
   cd gcp-terraform-project
   ```

2. **Configure your GCP credentials**:
   Ensure that your GCP credentials are set up. You can authenticate using the Google Cloud SDK:
   ```
   gcloud auth login
   ```

3. **Initialize Terraform**:
   Run the following command to initialize the Terraform configuration:
   ```
   terraform init
   ```

4. **Customize Variables**:
   Edit the `variables.tf` files in both the root and the module directory to customize the configuration as needed.

5. **Plan the deployment**:
   Generate an execution plan:
   ```
   terraform plan
   ```

6. **Apply the configuration**:
   Deploy the resources defined in the configuration:
   ```
   terraform apply
   ```

## Outputs

After the resources are created, you can view the outputs defined in the `outputs.tf` files to get information such as instance IP addresses or bucket names.

## Cleanup

To remove all resources created by this project, run:
```
terraform destroy
```

## License

This project is licensed under the MIT License.