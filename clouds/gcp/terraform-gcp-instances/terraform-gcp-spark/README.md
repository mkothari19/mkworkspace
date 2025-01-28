# Terraform GCP Spark Project

This project provisions a Spark instance on Google Cloud Platform (GCP) using Terraform. It includes all necessary configurations to set up a Dataproc cluster for running Spark jobs.

## Project Structure

- `main.tf`: Contains the main configuration for provisioning the Spark instance, including the Dataproc cluster settings.
- `variables.tf`: Defines input variables such as project ID, region, cluster name, and machine types.
- `outputs.tf`: Specifies output values displayed after resource creation, including cluster name and endpoint.
- `provider.tf`: Configures the GCP provider with authentication details and project settings.
- `README.md`: Documentation for setting up and running the Terraform scripts.

## Prerequisites

1. **Google Cloud Account**: Ensure you have a Google Cloud account and the necessary permissions to create resources.
2. **Terraform Installed**: Install Terraform on your local machine. You can download it from [terraform.io](https://www.terraform.io/downloads.html).
3. **GCP SDK**: Install the Google Cloud SDK to authenticate and interact with GCP services.

## Setup Instructions

1. **Clone the Repository**:
   ```
   git clone <repository-url>
   cd terraform-gcp-spark
   ```

2. **Configure Variables**:
   Update the `variables.tf` file with your GCP project ID, region, cluster name, and machine types.

3. **Initialize Terraform**:
   Run the following command to initialize the Terraform configuration:
   ```
   terraform init
   ```

4. **Plan the Deployment**:
   Generate an execution plan to see what resources will be created:
   ```
   terraform plan
   ```

5. **Apply the Configuration**:
   Apply the Terraform configuration to provision the Spark instance:
   ```
   terraform apply
   ```

6. **Access the Spark Cluster**:
   After the deployment is complete, use the output values to access your Spark cluster.

## Cleanup

To remove all resources created by this project, run:
```
terraform destroy
```

## License

This project is licensed under the MIT License. See the LICENSE file for details.