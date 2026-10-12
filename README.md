# 🍔 FoodRush – Android Food Delivery App
### Complete Setup Guide for Android Studio

---

## 📁 Project Structure

```
FoodRush/
├── app/
│   ├── src/main/
│   │   ├── java/com/foodrush/
│   │   │   ├── activities/
│   │   │   │   ├── SplashActivity.java
│   │   │   │   ├── LoginActivity.java
│   │   │   │   ├── SignupActivity.java
│   │   │   │   ├── MainActivity.java
│   │   │   │   └── OrderConfirmationActivity.java
│   │   │   ├── fragments/
│   │   │   │   ├── HomeFragment.java
│   │   │   │   ├── CartFragment.java
│   │   │   │   ├── OrdersFragment.java
│   │   │   │   └── ProfileFragment.java
│   │   │   ├── adapters/
│   │   │   │   ├── FoodAdapter.java
│   │   │   │   ├── CartAdapter.java
│   │   │   │   └── OrderAdapter.java
│   │   │   ├── models/
│   │   │   │   ├── FoodItem.java
│   │   │   │   ├── CartItem.java
│   │   │   │   ├── Order.java
│   │   │   │   └── User.java
│   │   │   ├── database/
│   │   │   │   ├── CartManager.java
│   │   │   │   └── FirebaseHelper.java
│   │   │   └── utils/
│   │   │       └── NotificationHelper.java
│   │   ├── res/
│   │   │   ├── layout/         (all XML layouts)
│   │   │   ├── menu/           (bottom_nav_menu.xml)
│   │   │   ├── drawable/       (icons + shapes)
│   │   │   ├── values/         (colors, strings, themes)
│   │   │   └── values-hi/      (Hindi strings)
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── google-services.json   ← REPLACE THIS
├── build.gradle
├── settings.gradle
└── gradle.properties
```

---

## 🔥 STEP 1 — Firebase Setup (DO THIS FIRST)

> Without this step the app will crash on launch.

1. Open **https://console.firebase.google.com**
2. Click **"Add project"** → name it `FoodRush` → Continue
3. Click **"Add app"** → choose **Android**
4. Enter package name exactly: `com.foodrush`
5. Click **"Register app"**
6. **Download `google-services.json`**
7. **Replace** the placeholder file at `app/google-services.json` with your downloaded file

### Enable Authentication
1. In Firebase Console → **Authentication** → **Get started**
2. Click **Email/Password** → Enable → Save

### Enable Realtime Database
1. In Firebase Console → **Realtime Database** → **Create database**
2. Choose your region → **Start in test mode** → Enable
3. Go to **Rules** tab and set:
```json
{
  "rules": {
    ".read": "auth != null",
    ".write": "auth != null"
  }
}
```

---

## 🛠 STEP 2 — Open in Android Studio

1. Open **Android Studio** (Hedgehog 2023.1.1 or newer recommended)
2. Click **"Open"** (not "New Project")
3. Navigate to the `FoodRush` folder → Click **OK**
4. Wait for Android Studio to index the project

---

## ⚙️ STEP 3 — Sync Gradle

1. Android Studio will show **"Gradle files have changed"** banner
2. Click **"Sync Now"**
3. Wait for sync to complete (needs internet — downloads dependencies)
4. If sync fails, go to **File → Sync Project with Gradle Files**

> ✅ Make sure you have Java 8+ and Android SDK 34 installed.
> Go to **File → Project Structure → SDK Location** to verify.

---

## ▶️ STEP 4 — Run on Emulator or Phone

### Option A: Emulator
1. Go to **Device Manager** (right panel or Tools → Device Manager)
2. Click **"Create Device"** → choose **Pixel 6** → Next
3. Select **API 34 (Android 14)** system image → Download if needed → Next → Finish
4. Click the **▶ Run** button (green triangle)

### Option B: Real Android Phone
1. On your phone: **Settings → About Phone → tap Build Number 7 times**
2. Go to **Settings → Developer Options → Enable USB Debugging**
3. Connect phone via USB → Allow debugging prompt on phone
4. Select your phone from the device dropdown in Android Studio
5. Click **▶ Run**

---

## 📦 STEP 5 — Generate APK

### Debug APK (for testing)
1. Menu: **Build → Build Bundle(s) / APK(s) → Build APK(s)**
2. Wait for build to finish
3. Click **"locate"** in the notification popup
4. APK is at: `app/build/outputs/apk/debug/app-debug.apk`
5. Copy this file to your phone and install it
   *(Allow "Install from unknown sources" in phone settings)*

### Release APK (for submission/distribution)
1. Menu: **Build → Generate Signed Bundle / APK**
2. Choose **APK** → Next
3. Click **"Create new..."** to create a keystore
4. Fill in keystore details → OK
5. Choose **release** build variant → Finish
6. APK is at: `app/build/outputs/apk/release/app-release.apk`

---

## 🌟 App Features Summary

| Feature | Implementation |
|---|---|
| Login / Signup | Firebase Authentication (Email + Password) |
| Food Menu | RecyclerView grid, data from Firebase Realtime DB |
| Search | Live search with TextWatcher |
| Category Filter | Horizontal scrollable chips |
| Cart | In-memory CartManager singleton, add/remove/delete |
| Order Placement | Saved to Firebase under `/orders/{userId}/` |
| Order History | Fetched from Firebase, sorted newest first |
| Notifications | Local notification on order placed |
| Profile | User info from Firebase DB, logout |
| Localization | English + Hindi (`values/` and `values-hi/`) |
| Navigation | Bottom Navigation + Fragment transactions |

---

## ⚡ First Launch Behaviour

- On **first run**: App seeds 10 sample food items into Firebase automatically
- Items appear in Home grid immediately after seeding
- Register a new account via Signup screen
- Add items to cart → Place order → See notification + confirmation screen
- Check Orders tab to see your order history

---

## 🐛 Common Issues & Fixes

| Problem | Fix |
|---|---|
| `google-services.json` error | Replace placeholder with real file from Firebase Console |
| `Default FirebaseApp is not initialized` | google-services.json is wrong/missing |
| Gradle sync fails | Check internet connection; try File → Invalidate Caches |
| App crashes on launch | Enable Email/Password auth in Firebase Console |
| No food items showing | Check Firebase Realtime Database rules (allow read/write) |
| Notification not showing | On Android 13+, grant notification permission when prompted |

---

*Built with ❤️ using Java + Firebase + Material Design*
