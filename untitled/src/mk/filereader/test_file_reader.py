import unittest
from file_read import read_file
import os


class TestFileReader(unittest.TestCase):
    def setUp(self):
        """Set up a test file."""
        self.test_file = 'test_data.txt'
        with open(self.test_file, 'w') as file:
            file.write("Line 1\nLine 2\nLine 3\n")

    def tearDown(self):
        """Clean up the test file."""
        if os.path.exists(self.test_file):
            os.remove(self.test_file)

    def test_read_file_success(self):
        """Test reading a file successfully."""
        content = read_file(self.test_file)
        self.assertEqual(content, ["Line 1\n", "Line 2\n", "Line 3\n"])

    def test_read_file_not_found(self):
        """Test reading a non-existent file."""
        with self.assertRaises(FileNotFoundError):
            read_file('non_existent_file.txt')

    def test_read_file_error(self):
        """Test error handling for unexpected exceptions."""
        with open(self.test_file, 'w') as file:
            os.chmod(self.test_file, 0o000)  # Remove read permissions
        with self.assertRaises(Exception):
            read_file(self.test_file)
        os.chmod(self.test_file, 0o644)  # Restore permissions for cleanup


if __name__ == "__main__":
    unittest.main()
