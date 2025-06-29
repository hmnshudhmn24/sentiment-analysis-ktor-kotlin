
# 💬 Sentiment Analysis Kotlin Web API

This project combines a Python-trained ONNX sentiment classifier with a blazing-fast Kotlin Ktor server. Classify text into positive, negative, or neutral categories with a simple JSON API. Collect user feedback for future retraining and easily scale your sentiment analysis microservice!

---

## ✨ Project Highlights

- Kotlin Ktor server with modern structure
- Serves an ONNX-based sentiment model
- Classifies positive / negative / neutral
- Feedback endpoint for future retraining
- Ready to integrate with PostgreSQL or SQLite
- JVM-deployable, production-ready

---

## 🚀 How to Run

1. **Train the sentiment model in Python**  
   Export it to ONNX:
   ```python
   import joblib
   joblib.dump(my_classifier, "sentiment.onnx")
   ```
   Place `sentiment.onnx` in `resources/`.

2. **Clone and build**

   ```bash
   git clone https://github.com/yourusername/sentiment-analysis-ktor-kotlin.git
   cd sentiment-analysis-ktor-kotlin
   ./gradlew run
   ```

3. **Test the API**
   - POST to `/analyze`:
     ```json
     { "text": "I love this product!" }
     ```
     returns
     ```json
     { "sentiment": "positive", "confidence": 0.95 }
     ```
   - POST to `/feedback`:
     ```json
     { "text": "this sucks" }
     ```

---

## 🛠 Customization

- Swap in new ONNX models by replacing `sentiment.onnx`  
- Add a database (PostgreSQL recommended) for active retraining  
- Expand to streaming sentiment scoring

---

## 🌟 Next Features

- Web dashboard for managing feedback  
- GPT fallback for advanced conversation  
- Multilingual support

---
