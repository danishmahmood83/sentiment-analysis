import {SentimentChart} from "../../stock-sentiment-ui/src/components/SentimentChart";
import SymbolChartViewer from "../../stock-sentiment-ui/src/components/SymbolChartViewer";

export default {
    title: "Symbol Chart Viewer",
    component: SymbolChartViewer
}

export const chart = () => <SymbolChartViewer symbol={selectedSymbol} ></SymbolChartViewer>