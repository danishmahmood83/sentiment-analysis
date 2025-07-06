import {SentimentChart} from "../../stock-sentiment-ui/src/components/SentimentChart";
import SymbolSearch from "../../stock-sentiment-ui/src/components/SymbolSearch";

export default {
    title: "Symbol Search",
    component: SymbolSearch
}

export const chart = () => <SymbolSearch symbol={selectedSymbol} ></SymbolSearch>