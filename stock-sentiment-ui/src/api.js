import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/sentiment';

export const fetchSentimentData = async (symbol) => {
  const response = await axios.get(`${BASE_URL}/${symbol}/summary`);
  return response.data;
};