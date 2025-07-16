import {SentimentChart} from "../components/SentimentChart";
import TrackedSymbolDropdown from "../components/TrackedSymbolDropdown";

export default {
    title: "Tracked Symbol Dropdown",
    component: TrackedSymbolDropdown
}
export const selectedSymbol = {
    args: {
        selectedSymbol: 'TSLA',
        // ... other props
    },
};
export const chart = () => <TrackedSymbolDropdown symbol={selectedSymbol} ></TrackedSymbolDropdown>