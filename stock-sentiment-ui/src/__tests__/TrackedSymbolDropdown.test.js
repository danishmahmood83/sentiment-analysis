import React from "react";
import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import { act } from 'react';
import axios from "axios";
import TrackedSymbolDropdown from "../components/TrackedSymbolDropdown";

jest.mock("axios");

describe("TrackedSymbolDropdown", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test("fetches and displays symbols on mount and refreshKey change", async () => {
    axios.get.mockResolvedValue({
      data: [
        { id: 1, symbol: "AAPL" },
        { id: 2, symbol: "GOOG" },
      ],
    });

    await act(async () => {
      render(<TrackedSymbolDropdown refreshKey={0} />);
    });

    await waitFor(() => {
      expect(screen.getByText("AAPL")).toBeInTheDocument();
      expect(screen.getByText("GOOG")).toBeInTheDocument();
    });

    expect(axios.get).toHaveBeenCalledWith("http://localhost:8080/api/tracked");
  });

  test("shows remove button only when a symbol is selected", async () => {
    axios.get.mockResolvedValue({ data: [{ id: 1, symbol: "AAPL" }] });

    await act(async () => {
      render(<TrackedSymbolDropdown refreshKey={0} />);
    });

    await waitFor(() => screen.getByText("AAPL"));

    // Initially no remove button
    expect(screen.queryByRole("button", { name: /remove/i })).not.toBeInTheDocument();

    // Select symbol (wrap in act)
    await act(async () => {
      fireEvent.change(screen.getByRole("combobox"), { target: { value: "AAPL" } });
    });

    expect(screen.getByRole("button", { name: /remove/i })).toBeInTheDocument();

    // Deselect symbol
    await act(async () => {
      fireEvent.change(screen.getByRole("combobox"), { target: { value: "" } });
    });

    expect(screen.queryByRole("button", { name: /remove/i })).not.toBeInTheDocument();
  });

  test("calls axios.delete and onSymbolRemoved when remove button clicked", async () => {
    const onSymbolRemoved = jest.fn();

    axios.get.mockResolvedValue({ data: [{ id: 1, symbol: "AAPL" }] });
    axios.delete.mockResolvedValue({});

    await act(async () => {
      render(<TrackedSymbolDropdown refreshKey={0} onSymbolRemoved={onSymbolRemoved} />);
    });

    await waitFor(() => screen.getByText("AAPL"));

    await act(async () => {
      fireEvent.change(screen.getByRole("combobox"), { target: { value: "AAPL" } });
    });

    const removeBtn = screen.getByRole("button", { name: /remove/i });

    await act(async () => {
      fireEvent.click(removeBtn);
    });

    await waitFor(() => {
      expect(axios.delete).toHaveBeenCalledWith("http://localhost:8080/api/tracked/AAPL");
      expect(onSymbolRemoved).toHaveBeenCalled();
      expect(screen.queryByRole("button", { name: /remove/i })).not.toBeInTheDocument();
    });
  });

  test("does not call delete if no symbol selected", async () => {
    axios.get.mockResolvedValue({ data: [] });
    axios.delete.mockResolvedValue({});

    await act(async () => {
      render(<TrackedSymbolDropdown refreshKey={0} />);
    });

    expect(screen.queryByRole("button", { name: /remove/i })).not.toBeInTheDocument();
  });

  test("logs error on fetch failure", async () => {
    const consoleErrorSpy = jest.spyOn(console, "error").mockImplementation(() => {});
    axios.get.mockRejectedValue(new Error("Fetch failed"));

    await act(async () => {
      render(<TrackedSymbolDropdown refreshKey={0} />);
    });

    await waitFor(() => {
      expect(consoleErrorSpy).toHaveBeenCalledWith(
        "Error fetching tracked symbols:",
        expect.any(Error)
      );
    });

    consoleErrorSpy.mockRestore();
  });

  test("logs error on delete failure", async () => {
    const consoleErrorSpy = jest.spyOn(console, "error").mockImplementation(() => {});
    axios.get.mockResolvedValue({ data: [{ id: 1, symbol: "AAPL" }] });
    axios.delete.mockRejectedValue(new Error("Delete failed"));

    await act(async () => {
      render(<TrackedSymbolDropdown refreshKey={0} />);
    });

    await waitFor(() => screen.getByText("AAPL"));

    await act(async () => {
      fireEvent.change(screen.getByRole("combobox"), { target: { value: "AAPL" } });
    });

    const removeBtn = screen.getByRole("button", { name: /remove/i });

    await act(async () => {
      fireEvent.click(removeBtn);
    });

    await waitFor(() => {
      expect(consoleErrorSpy).toHaveBeenCalledWith(
        "Error removing symbol:",
        expect.any(Error)
      );
    });

    consoleErrorSpy.mockRestore();
  });
});
