import {SentimentChart} from "../components/SentimentChart";
import SymbolSearch from "../components/SymbolSearch";

export default {
    title: "Symbol Search",
    component: SymbolSearch
}
export const selectedSymbol = {
    args: {
        selectedSymbol: 'TSLA',
        // ... other props
    },
};
export const chart = () => <SymbolSearch symbol={selectedSymbol} ></SymbolSearch>