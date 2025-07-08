import {SentimentChart} from "../components/SentimentChart";

export default {
    title: "Sentiment Chart",
    component: SentimentChart
}

const Template = {
    args: {
        selectedSymbol: 'TSLA',
        analysisMethod: 'gpt',
        // ... other props
    },
};

//export const chart = () => <SentimentChart symbol={selectedSymbol, analysisMethod} ></SentimentChart>
export const chart = Template.bind({})
chart.args = {
    selectedSymbol: 'TSLA',
    analysisMethod: 'gpt',
}