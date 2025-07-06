import {SentimentChart} from "../components/SentimentChart";
import SymbolChartViewer from "../components/SymbolChartViewer";

export default {
    title: "Symbol Chart Viewer",
    component: SymbolChartViewer
}

export const selectedSymbol = {
    args: {
        selectedSymbol: 'TSLA',
        // ... other props
    },
};
export const chart = () => <SymbolChartViewer symbol={selectedSymbol} ></SymbolChartViewer>