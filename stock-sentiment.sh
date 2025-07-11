
# 1. Generate Javadoc (Maven example)
mvn clean javadoc:javadoc

# 2. Navigate to output directory
cd target/site/apidocs

# 3. Start local server
python -m http.server 80

# 4. Open browser to http://localhost:8080
open "http://localhost:80"

# Start Kafka Container
docker run -p 9092:9092 apache/kafka:4.0.0

# Start React
cd stock-sentiment-ui
npm start

# Start Storybook
npm run storybook
