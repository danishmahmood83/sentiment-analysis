import React, { useEffect, useState } from "react";
import axios from "axios";

const NotificationPanel = () => {
  const [notifications, setNotifications] = useState([]);
  const [fadingOut, setFadingOut] = useState([]);

  const dismissNotification = (id) => {
    setFadingOut((prev) => [...prev, id]);

    setTimeout(async () => {
      try {
        await axios.post(`http://localhost:8080/api/notifications/${id}/viewed`);
      } catch (error) {
        console.error("Failed to mark notification viewed:", error);
      }

      setNotifications((prev) => prev.filter((notif) => notif.id !== id));
      setFadingOut((prev) => prev.filter((fid) => fid !== id));
    }, 500); // match animation duration
  };

  const fetchNotifications = async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/notifications");
      setNotifications(res.data);
    } catch (error) {
      console.error("Failed to fetch notifications:", error);
    }
  };

  useEffect(() => {
    notifications.forEach((notif) => {
      const timer = setTimeout(() => {
        dismissNotification(notif.id);
      }, 20000);
      return () => clearTimeout(timer);
    });
  }, [notifications]);

  useEffect(() => {
    fetchNotifications();
    const interval = setInterval(fetchNotifications, 5000);
    return () => clearInterval(interval);
  }, []);

  // const dismissNotification = async (id) => {
  //   try {
  //     await axios.post(`http://localhost:8080/api/notifications/${id}/viewed`);
  //     setNotifications(notifications.filter((notif) => notif.id !== id));
  //   } catch (error) {
  //     console.error("Failed to mark notification viewed:", error);
  //   }
  // };

  if (notifications.length === 0) return null;

  return (
    <div style={{ position: "fixed", top: 10, right: 10, width: 320, zIndex: 1000 }}>
      {notifications.map((notif) => (
          <div
              key={notif.id}
              style={{
                background: "#eee",
                padding: 10,
                marginBottom: 8,
                borderRadius: 4,
                boxShadow: "0 2px 6px rgba(0,0,0,0.2)",
              }}
          >
            <b>{notif.symbol}</b> sentiment reached <b>{notif.sentiment}</b> with  {notif.percent.toFixed(2)} %
            <br />
            <small>{new Date(notif.timestamp).toLocaleTimeString()}</small>
            <button
                onClick={() => dismissNotification(notif.id)}
                style={{ marginLeft: 10, cursor: "pointer" }}
            >
              Dismiss
            </button>
          </div>
      ))}

    </div>
  );
};

export default NotificationPanel;
