import unittest
from select_firebase_device import choose_device

CATALOG = [
    {"id": "vphone", "form": "VIRTUAL", "formFactor": "PHONE",
     "supportedVersionIds": ["35"], "name": "Virtual device"},
    {"id": "tablet", "form": "PHYSICAL", "formFactor": "TABLET",
     "supportedVersionIds": ["35"], "name": "Tablet"},
    {"id": "phys_1", "form": "PHYSICAL", "formFactor": "PHONE",
     "supportedVersionIds": ["33", "34"], "manufacturer": "Google", "name": "Phone"},
    {"id": "phys_2", "form": "PHYSICAL", "formFactor": "PHONE",
     "supportedVersionIds": ["33"], "manufacturer": "Other", "name": "Other phone"},
]

class DeviceSelectorTests(unittest.TestCase):
    def test_selects_only_physical_phone(self):
        self.assertEqual(choose_device(CATALOG)[:2], ("phys_1", "34"))

    def test_requested_compatible_physical_device(self):
        self.assertEqual(choose_device(CATALOG, "phys_2", "33")[:2],
                         ("phys_2", "33"))

    def test_rejects_virtual_device(self):
        with self.assertRaises(ValueError):
            choose_device(CATALOG, "vphone", "35")

    def test_rejects_missing_version(self):
        with self.assertRaises(ValueError):
            choose_device(CATALOG, "phys_1", "")

    def test_rejects_missing_device(self):
        with self.assertRaises(ValueError):
            choose_device([], "", "")

if __name__ == "__main__":
    unittest.main()
