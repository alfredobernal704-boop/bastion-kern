import unittest
from verify_firebase_config import verify


def sample():
    return {
        "project_info": {"project_id": "bastion-sdk", "project_number": "123"},
        "client": [
            {"client_info": {
                "android_client_info": {"package_name": "app.bastionkern"},
                "mobilesdk_app_id": "1:123:android:abcdef"
            }}
        ]
    }


class VerifyFirebaseConfigTests(unittest.TestCase):
    def test_valid_registered_client(self):
        self.assertTrue(verify(sample()))

    def test_wrong_project_rejected(self):
        data = sample()
        data["project_info"]["project_id"] = "different-project"
        with self.assertRaises(ValueError):
            verify(data)

    def test_wrong_android_application_rejected(self):
        data = sample()
        data["client"][0]["client_info"]["android_client_info"]["package_name"] = "another.app"
        with self.assertRaises(ValueError):
            verify(data)

    def test_missing_app_id_rejected(self):
        data = sample()
        del data["client"][0]["client_info"]["mobilesdk_app_id"]
        with self.assertRaises(ValueError):
            verify(data)

    def test_duplicate_clients_rejected(self):
        data = sample()
        data["client"].append(data["client"][0])
        with self.assertRaises(ValueError):
            verify(data)


if __name__ == "__main__":
    unittest.main()
