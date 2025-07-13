import SentimentChart from "../components/SentimentChart";

// Mock data that matches your API response format
const mockSentimentData = {
    bullish: 45,
    bearish: 20,
    neutral: 35
};

const mockEmptyData = {
    bullish: 0,
    bearish: 0,
    neutral: 0
};

// Mock fetch function
const mockFetch = (mockData) => {
    return jest.fn().mockResolvedValue({
        ok: true,
        json: () => Promise.resolve(mockData)
    });
};

// Setup global fetch mock
const setupMockFetch = (mockData) => {
    global.fetch = mockFetch(mockData);
};

export default {
    title: "Sentiment Chart",
    component: SentimentChart,
    decorators: [
        (Story) => {
            // Setup default mock data
            setupMockFetch(mockSentimentData);
            return <Story />;
        }
    ]
};

// Basic story with default mock data
export const Default = {
    args: {
        symbol: 'TSLA',
    },
};

// Story with GPT analysis method
export const WithGPTAnalysis = {
    args: {
        symbol: 'TSLA',
        analysisMethod: 'gpt',
    },
};

// Story with high bullish sentiment
export const HighBullish = {
    decorators: [
        (Story) => {
            setupMockFetch(mockHighBullishData);
            return <Story />;
        }
    ],
    args: {
        symbol: 'AAPL',
    },
};

// Story with high bearish sentiment
export const HighBearish = {
    decorators: [
        (Story) => {
            setupMockFetch(mockHighBearishData);
            return <Story />;
        }
    ],
    args: {
        symbol: 'GOOGL',
    },
};

// Story with balanced sentiment
export const Balanced = {
    decorators: [
        (Story) => {
            setupMockFetch(mockBalancedData);
            return <Story />;
        }
    ],
    args: {
        symbol: 'MSFT',
    },
};

// Story with no data
export const NoData = {
    decorators: [
        (Story) => {
            setupMockFetch(mockEmptyData);
            return <Story />;
        }
    ],
    args: {
        symbol: 'AMZN',
    },
};

// Story with API error
export const APIError = {
    decorators: [
        (Story) => {
            global.fetch = jest.fn().mockRejectedValue(new Error('API Error'));
            return <Story />;
        }
    ],
    args: {
        symbol: 'NFLX',
    },
};

// Interactive story with controls
export const Interactive = {
    args: {
        symbol: 'TSLA',
        analysisMethod: undefined,
    },
    argTypes: {
        symbol: {
            control: { type: 'select' },
            options: ['AAPL', 'GOOGL', 'TSLA', 'MSFT', 'AMZN'],
        },
        analysisMethod: {
            control: { type: 'select' },
            options: [undefined, 'gpt', 'basic', 'advanced'],
        },
    },
};