import {SentimentChart} from "../components/SentimentChart";

export default {
    title: "Sentiment Chart",
    component: SentimentChart
}
export const selectedSymbol = {
    args: {
        selectedSymbol: 'TSLA',
        // ... other props
    },
};
export const chart = () => <SentimentChart symbol={selectedSymbol} ></SentimentChart>