import {SentimentChart} from "../../stock-sentiment-ui/src/components/SentimentChart";

export default {
    title: "Sentiment Chart",
    component: SentimentChart
}

export const chart = () => <SentimentChart symbol={selectedSymbol} ></SentimentChart>