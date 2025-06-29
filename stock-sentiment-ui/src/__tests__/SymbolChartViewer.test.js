import React from "react";
import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import SymbolChartViewer from "../components/SymbolChartViewer"; // Adjust path if needed

// Mock SentimentChart to avoid rendering the real chart
jest.mock("../components/SentimentChart", () => ({ symbol, analysisMethod }) => (
  <div data-testid="sentiment-chart">
    Chart for {symbol} - {analysisMethod}
  </div>
));

describe("SymbolChartViewer Component", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test("fetches and renders symbols on mount", async () => {
    const mockSymbols = [
      { id: 1, symbol: "AAPL" },
      { id: 2, symbol: "GOOG" },
    ];
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockSymbols),
      })
    );

    render(<SymbolChartViewer refreshKey={0} />);

    // Wait for options to appear
    await waitFor(() => {
      expect(screen.getByText("AAPL")).toBeInTheDocument();
      expect(screen.getByText("GOOG")).toBeInTheDocument();
    });

    expect(global.fetch).toHaveBeenCalledWith("http://localhost:8080/api/tracked");
  });

  test("logs error if fetch fails", async () => {
    const consoleErrorSpy = jest.spyOn(console, "error").mockImplementation(() => {});
    global.fetch = jest.fn(() => Promise.resolve({ ok: false }));

    render(<SymbolChartViewer refreshKey={0} />);

    await waitFor(() => {
      expect(consoleErrorSpy).toHaveBeenCalledWith(
        "Error fetching tracked symbols:",
        expect.any(Error)
      );
    });

    consoleErrorSpy.mockRestore();
  });

  test("selecting a symbol shows analysis methods", async () => {
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve([{ id: 1, symbol: "AAPL" }]),
      })
    );

    render(<SymbolChartViewer refreshKey={0} />);

    // Wait for symbol option and select it
    await waitFor(() => screen.getByText("AAPL"));

    fireEvent.change(screen.getByRole("combobox"), { target: { value: "AAPL" } });

    expect(screen.getByText(/Select Analysis Methods to Compare/i)).toBeInTheDocument();

    // Check that all AVAILABLE_METHODS checkboxes are rendered
    ["finbert", "gpt", "stanford"].forEach(method => {
      expect(screen.getByLabelText(method)).toBeInTheDocument();
      expect(screen.getByLabelText(method).checked).toBe(false);
    });
  });

  test("shows message when no analysis method is selected", async () => {
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve([{ id: 1, symbol: "AAPL" }]),
      })
    );

    render(<SymbolChartViewer refreshKey={0} />);

    await waitFor(() => screen.getByText("AAPL"));

    fireEvent.change(screen.getByRole("combobox"), { target: { value: "AAPL" } });

    expect(screen.getByText(/Select at least one analysis method to view charts/i)).toBeInTheDocument();
  });

  test("toggling checkboxes renders SentimentChart components", async () => {
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve([{ id: 1, symbol: "AAPL" }]),
      })
    );

    render(<SymbolChartViewer refreshKey={0} />);

    await waitFor(() => screen.getByText("AAPL"));

    fireEvent.change(screen.getByRole("combobox"), { target: { value: "AAPL" } });

    const finbertCheckbox = screen.getByLabelText("finbert");
    const gptCheckbox = screen.getByLabelText("gpt");

    // Initially no charts
    expect(screen.queryAllByTestId("sentiment-chart")).toHaveLength(0);

    // Select finbert
    fireEvent.click(finbertCheckbox);
    expect(finbertCheckbox.checked).toBe(true);
    expect(screen.getAllByTestId("sentiment-chart")).toHaveLength(1);
    expect(screen.getByText("Chart for AAPL - finbert")).toBeInTheDocument();

    // Select gpt as well
    fireEvent.click(gptCheckbox);
    expect(gptCheckbox.checked).toBe(true);
    expect(screen.getAllByTestId("sentiment-chart")).toHaveLength(2);
    expect(screen.getByText("Chart for AAPL - gpt")).toBeInTheDocument();

    // Deselect finbert
    fireEvent.click(finbertCheckbox);
    expect(finbertCheckbox.checked).toBe(false);
    expect(screen.getAllByTestId("sentiment-chart")).toHaveLength(1);
    expect(screen.queryByText("Chart for AAPL - finbert")).not.toBeInTheDocument();
  });

  test("changing refreshKey triggers refetch", async () => {
    const firstFetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve([{ id: 1, symbol: "AAPL" }]),
      })
    );

    const secondFetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve([{ id: 2, symbol: "GOOG" }]),
      })
    );

    global.fetch = firstFetch;

    const { rerender } = render(<SymbolChartViewer refreshKey={0} />);

    await waitFor(() => screen.getByText("AAPL"));

    // Update fetch mock for next render
    global.fetch = secondFetch;

    rerender(<SymbolChartViewer refreshKey={1} />);

    await waitFor(() => screen.getByText("GOOG"));
  });
});
