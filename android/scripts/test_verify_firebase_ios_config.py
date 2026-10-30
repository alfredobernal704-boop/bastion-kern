import unittest
from verify_firebase_ios_config import verify

def sample():
    return {
        "PROJECT_ID": "bastion-sdk",
        "GCM_SENDER_ID": "158994318096",
        "BUNDLE_ID": "Bastionkern.offentlogspot.com",
        "GOOGLE_APP_ID": "1:158994318096:ios:abcdef",
    }

class FirebaseIOSConfigTests(unittest.TestCase):
    def test_valid_registered_client(self):
        self.assertTrue(verify(sample()))

    def test_wrong_ios_bundle_rejected(self):
        x = sample()
        x["BUNDLE_ID"] = "com.offentlogspot.bastionkern"
        with self.assertRaises(ValueError):
            verify(x)

    def test_wrong_project_rejected(self):
        x = sample()
        x["PROJECT_ID"] = "other"
        with self.assertRaises(ValueError):
            verify(x)

    def test_invalid_ios_app_id_rejected(self):
        x = sample()
        x["GOOGLE_APP_ID"] = "1:158994318096:android:xyz"
        with self.assertRaises(ValueError):
            verify(x)

if __name__ == "__main__":
    unittest.main()
