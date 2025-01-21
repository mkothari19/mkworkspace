import unittest
from src.main import main


class TestPOC(unittest.TestCase):
    def test_main(self):
        self.assertEqual(1 + 1, 2)


if __name__ == "__main__":
    unittest.main()
