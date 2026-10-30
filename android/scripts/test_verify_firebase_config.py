import unittest
from verify_firebase_config import verify

def sample():
    return {
        "project_info": {
            "project_id": "bastion-sdk",
            "project_number": "158994318096"
        },
        "client": [{
            "client_info": {
                "android_client_info": {"package_name": "app.bastionkern"},
                "mobilesdk_app_id": "1:158994318096:android:abcdef",
            },
            "api_key": [{"current_key": "test-public-key"}],
        }],
    }

class VerifyFirebaseConfigTests(unittest.TestCase):
    def test_valid_registered_client(self):
        self.assertTrue(verify(sample()))

    def test_wrong_project_rejected(self):
        x = sample()
        x["project_info"]["project_id"] = "another-project"
        with self.assertRaises(ValueError):
            verify(x)

    def test_wrong_project_number_rejected(self):
        x = sample()
        x["project_info"]["project_number"] = "999999999999"
        with self.assertRaises(ValueError):
            verify(x)

    def test_wrong_android_application_rejected(self):
        x = sample()
        x["client"][0]["client_info"]["android_client_info"]["package_name"] = "Bastionkern.com"
        with self.assertRaisesRegex(ValueError, "Android package mismatch"):
            verify(x)

    def test_missing_app_id_rejected(self):
        x = sample()
        del x["client"][0]["client_info"]["mobilesdk_app_id"]
        with self.assertRaises(ValueError):
            verify(x)

    def test_duplicate_clients_rejected(self):
        x = sample()
        x["client"].append(x["client"][0])
        with self.assertRaises(ValueError):
            verify(x)

    def test_incorrect_platform_app_id_rejected(self):
        x = sample()
        x["client"][0]["client_info"]["mobilesdk_app_id"] = "1:158994318096:ios:abcdef"
        with self.assertRaises(ValueError):
            verify(x)

if __name__ == "__main__":
    unittest.main()
