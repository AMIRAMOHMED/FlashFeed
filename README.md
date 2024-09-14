# 🚀 FlashFeed
Welcome to FlashFeed – your go-to Android app for staying updated with the latest news in style! 📲📰 This app may be simple, but it’s packed with powerful features and cutting-edge tech to make your news browsing experience smooth and fun. Whether you’re a tech enthusiast, a news junkie, or just someone who loves a clean, intuitive app, FlashFeed has something for you! 🎉

## 🎯 Features
Here’s what FlashFeed brings to the table:

🔍 Fetch news by category: Stay in the loop with the latest news, from tech to sports and everything in between!

📰 View article details: Dive deep into any article with a beautifully designed details page.

❤️ Save your favorite articles: Like something you read? Save it for later, and access it offline anytime.

📂 Manage saved articles: Review, revisit, or delete your saved articles with just a tap.

🛠 Unit testing: We take stability seriously. This app is rigorously tested, ensuring a smooth experience every time you use it!

## 🌟 Technologies That Power FlashFeed
Under the hood, FlashFeed is driven by advanced Android technologies to ensure an optimal user experience:

🛠 MVVM Architecture: We believe in clean and scalable code, separating UI logic from business logic for better maintainability.

🌐 Retrofit: News feeds are fetched seamlessly with this powerhouse library for smooth API integration.

⏳ Kotlin Coroutines: No more waiting! We handle background tasks asynchronously to keep the app fast and responsive.

🗄 Room Database: All your saved articles are securely stored locally so you can access them offline – your news, always at your fingertips.

## 📸 Screenshots


<p align="center">
  <img src="https://github.com/user-attachments/assets/f6852977-0dc2-47bc-a4f5-a58129356f37" alt="Screen 1" width="200"/>
  <img src="https://github.com/user-attachments/assets/2cdad040-ecc9-4f74-b416-4a05c7ea7476" alt="Screen 3" width="200"/>
  <img src="https://github.com/user-attachments/assets/260ae009-ce05-4e47-b7c6-988635700901" alt="Screen 5" width="200"/>
  <img src="https://github.com/user-attachments/assets/b7e5e5d5-3776-4be7-92b3-037ca060ef0a" alt="Screen 6" width="200"/>
  <img src="https://github.com/user-attachments/assets/cc44a5d8-f83d-488e-a1a7-331cbbb5537a" alt="Screen 2" width="200"/>
</p>



## 🏗 Architecture Overview
FlashFeed follows the MVVM (Model-View-ViewModel) architectural pattern to ensure clean and modular code. By adhering to this architecture, we’ve made the app easy to extend and maintain as it grows!

## Key Components
View: Displays data to the user and handles UI interactions.
ViewModel: Manages UI-related data and acts as the middleman between the UI and business logic.
Repository: Fetches data from APIs or Room database and provides it to the ViewModel.
Retrofit Service: Makes network calls to fetch fresh news content from the API.
Room Database: Saves and retrieves articles locally for offline access.

🧪 Unit Testing
We didn’t stop at just building the app – we made sure it’s rock-solid! 💪

FlashFeed comes with unit tests to guarantee that the Room database operates flawlessly. From saving to retrieving and deleting articles, every operation is tested to ensure you have a reliable experience every time.
