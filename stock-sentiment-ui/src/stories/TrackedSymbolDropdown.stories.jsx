import {SentimentChart} from "../../stock-sentiment-ui/src/components/SentimentChart";
import TrackedSymbolDropdown from "../../stock-sentiment-ui/src/components/TrackedSymbolDropdown";

export default {
    title: "Tracked Symbol Dropdown",
    component: TrackedSymbolDropdown
}

export const chart = () => <TrackedSymbolDropdown symbol={selectedSymbol} ></TrackedSymbolDropdown>