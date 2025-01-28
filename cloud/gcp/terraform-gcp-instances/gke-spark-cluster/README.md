# GKE Spark Cluster Terraform Project

This project provisions a Google Kubernetes Engine (GKE) Spark cluster on Google Cloud Platform (GCP) using Terraform. 

## Project Structure

The project is organized as follows:

```
gke-spark-cluster
├── modules
│   └── gke
│       ├── main.tf         # Module configuration for GKE cluster
│       ├── outputs.tf      # Outputs specific to the GKE module
│       ├── variables.tf    # Variable definitions for the GKE module
│       └── versions.tf     # Required provider versions for the GKE module
├── main.tf                 # Main configuration for the Terraform project
├── outputs.tf              # Outputs of the Terraform configuration
├── variables.tf            # Variable definitions for the Terraform project
├── versions.tf             # Required provider versions for the project
└── README.md               # Project documentation
```

## Prerequisites

- Terraform installed on your local machine.
- Google Cloud SDK installed and configured.
- A GCP project with billing enabled.

## Setup Instructions

1. **Clone the repository:**
   ```
   git clone <repository-url>
   cd gke-spark-cluster
   ```

2. **Configure your variables:**
   Update the `variables.tf` file with your GCP project ID, desired region, and other configurations.

3. **Initialize Terraform:**
   Run the following command to initialize the Terraform configuration:
   ```
   terraform init
   ```

4. **Plan the deployment:**
   Generate an execution plan to see what resources will be created:
   ```
   terraform plan
   ```

5. **Apply the configuration:**
   Deploy the GKE Spark cluster by applying the configuration:
   ```
   terraform apply
   ```

6. **Access your GKE cluster:**
   After the deployment is complete, you can access your GKE cluster using the `kubectl` command-line tool.

## Outputs

After the deployment, the following outputs will be displayed:
- GKE Cluster Name
- Cluster Endpoint
- Node Pool Details

## Cleanup

To remove all resources created by this project, run:
```
terraform destroy
```

## License

This project is licensed under the MIT License.