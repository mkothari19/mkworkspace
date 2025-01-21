def read_file(file_path):
    """Read file and return  its content"""
    try:
        with open(file_path, 'r') as file:
            return file.readlines()
    except FileNotFoundError:
        raise FileNotFoundError(f"file not found error:{file_path}")
    except Exception as e:
        raise Exception(f"an error occurred:{e}")
