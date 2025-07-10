import {SentimentChart} from "../components/SentimentChart";

export default {
    title: "Sentiment Chart",
    component: SentimentChart
}

const selectedSymbol = {
    args: {
        selectedSymbol: 'TSLA',
        // analysisMethod: 'gpt',
        // ... other props
    },
};

export const chart = () => <SentimentChart symbol={selectedSymbol} ></SentimentChart>
