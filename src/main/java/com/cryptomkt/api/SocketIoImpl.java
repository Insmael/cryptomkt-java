package com.cryptomkt.api;

import com.cryptomkt.api.entity.SocAuthResponse;
import com.cryptomkt.api.utils.*;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.engineio.client.Transport;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SocketIoImpl implements SocketIo {
    //logging
    Logger logger;

    // socket fields
    String url_worker = "https://worker.cryptomkt.com";
    IO.Options opts;
    Socket socket;

    //data fields
    private JSONObject currenciesData;
    private JSONObject balanceData;
    private JSONObject openOrdersData;
    private JSONObject historicalOrdersData;
    private JSONObject operatedData;
    private JSONObject openBookData;
    private JSONObject historicalBookData;
    private JSONObject candlesData;
    private JSONObject tickerData;

    final SyncJson currenciesPub;
    final SyncJson balancePub;
    final SyncJson openOrdersPub;
    final SyncJson historicalOrdersPub;
    final SyncJson operatedPub;
    final SyncJson openBookPub;
    final SyncJson historicalBookPub;
    final SyncJson tickerPub;
    final SyncJson candlePub;

    //connect

    public SocketIoImpl(SocAuthResponse authToken) throws URISyntaxException {
        logger = Logger.getLogger(this.getClass().getName());
        logger.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.WARNING);
        logger.addHandler(handler);
        currenciesPub = new SyncJson();
        balancePub = new SyncJson();
        openOrdersPub = new SyncJson();
        historicalOrdersPub = new SyncJson();
        operatedPub = new SyncJson();
        openBookPub = new SyncJson();
        historicalBookPub = new SyncJson();
        tickerPub = new SyncJson();
        candlePub = new SyncJson();

        historicalBookData = new JSONObject();
        openBookData = new JSONObject();
        candlesData = new JSONObject();

        opts = new IO.Options();
        opts.reconnection = true;
        opts.reconnectionAttempts = 5;
        opts.reconnectionDelay = 1000;
        opts.reconnectionDelayMax = 15000;
        opts.transports = new String[]{"websocket"};
        System.out.println("creating socket");
        socket = IO.socket(url_worker, opts);
        socket.io().on(Manager.EVENT_TRANSPORT, args -> {
            Transport transport = (Transport) args[0];
            logger.info(transport.toString());
        });
        socket.on(Socket.EVENT_CONNECT, args -> {
            logger.fine("connected");
            JSONObject obj = new JSONObject();
            try {
                obj.put("socid", authToken.getSocAuth().getSocid());
                obj.put("uid", authToken.getSocAuth().getUid());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("user-auth", obj);
        }).on(Socket.EVENT_DISCONNECT, args -> logger.fine("disconnected")
        ).on("currencies", args -> {
            logger.fine("currencies data received");
            logger.fine(args[0].toString());
            currenciesData = (JSONObject) args[0];
            String jsonString = currenciesData.toString();
            synchronized (currenciesPub) {
                currenciesPub.setData(jsonString);
                currenciesPub.notifyAll();
            }
        }).on("currencies-delta", args -> {
            logger.fine("currencies delta received");
            logger.fine(args[0].toString());
            JSONObject patch = (JSONObject) args[0];
            try {
                if (!Objects.equals(currenciesData.get("to_tx"), (patch.get("from_tx")))) {
                    logger.fine("received tx ahead of current tx, retrieving data again");
                    //socket.emit("");
                    return;
                }
                currenciesData.put("data", JSONPatcher.patch(currenciesData.get("data"), patch.get("delta_data")));
                currenciesData.put("to_tx", patch.get("to_tx"));
                String jsonString = currenciesData.toString();
                synchronized (currenciesPub) {
                    currenciesPub.setData(jsonString);
                    currenciesPub.notifyAll();
                }

            } catch (JSONException | JSONPatchException e) {
                e.printStackTrace();
            }
        }).on("balance", args -> {
            logger.fine("balance data received");
            logger.fine(args[0].toString());
            balanceData = (JSONObject) args[0];
            String jsonString = null;
            try {
                jsonString = balanceDataToSend().toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            synchronized (balancePub) {
                balancePub.setData(jsonString);
                balancePub.notifyAll();
            }
        }).on("balance-delta", args -> {
            logger.fine("balance delta received");
            logger.fine(args[0].toString());
            JSONObject patch = (JSONObject) args[0];
            try {
                if (!Objects.equals(balanceData.get("to_tx"), (patch.get("from_tx")))) {
                    logger.fine("received tx ahead of current tx, retrieving data again");
                    //socket.emit("");
                    return;
                }
                balanceData.put("data", JSONPatcher.patch(balanceData.get("data"), patch.get("delta_data")));
                balanceData.put("to_tx", patch.get("to_tx"));
                String jsonString = balanceDataToSend().toString();
                synchronized (balancePub) {
                    balancePub.setData(jsonString);
                    balancePub.notifyAll();
                }

            } catch (JSONException | JSONPatchException e) {
                e.printStackTrace();
            }
        }).on("open-orders", args -> {
            logger.fine("open orders data received");
            logger.fine(args[0].toString());
            openOrdersData = (JSONObject) args[0];
            String jsonString = openOrdersData.toString();
            synchronized (openOrdersPub) {
                openOrdersPub.setData(jsonString);
                openOrdersPub.notifyAll();
            }
        }).on("open-orders-delta", args -> {
            logger.fine("open orders delta received");
            logger.fine(args[0].toString());
            JSONObject patch = (JSONObject) args[0];
            try {
                if (!Objects.equals(openOrdersData.get("to_tx"), (patch.get("from_tx")))) {
                    logger.fine("received tx ahead of current tx, retrieving data again");
                    //socket.emit("");
                    return;
                }
                openOrdersData.put("data", JSONPatcher.patch(openOrdersData.get("data"), patch.get("delta_data")));
                openOrdersData.put("to_tx", patch.get("to_tx"));

                String jsonString = openOrdersData.toString();
                synchronized (openOrdersPub) {
                    openOrdersPub.setData(jsonString);
                    openOrdersPub.notifyAll();
                }

            } catch (JSONException | JSONPatchException e) {
                e.printStackTrace();
            }
        }).on("historical-orders", args -> {
            logger.fine("historical orders data received");
            logger.fine(args[0].toString());
            historicalOrdersData = (JSONObject) args[0];
            String jsonString = historicalOrdersData.toString();
            synchronized (historicalOrdersPub) {
                historicalOrdersPub.setData(jsonString);
                historicalOrdersPub.notifyAll();
            }
        }).on("historical-orders-delta", args -> {
            logger.fine("historical orders delta received");
            logger.fine(args[0].toString());
            JSONObject patch = (JSONObject) args[0];
            try {
                if (!Objects.equals(historicalOrdersData.get("to_tx"), (patch.get("from_tx")))) {
                    logger.fine("received tx ahead of current tx, retrieving data again");
                    //socket.emit("");
                    return;
                }
                historicalOrdersData.put("data", JSONPatcher.patch(historicalOrdersData.get("data"), patch.get("delta_data")));
                historicalOrdersData.put("to_tx", patch.get("to_tx"));

                String jsonString = historicalOrdersData.toString();
                synchronized (historicalOrdersPub) {
                    historicalOrdersPub.setData(jsonString);
                    historicalOrdersPub.notifyAll();
                }

            } catch (JSONException | JSONPatchException e) {
                e.printStackTrace();
            }
        }).on("operated", args -> {
            logger.fine("operated data received");
            logger.fine(args[0].toString());
            operatedData = (JSONObject) args[0];
            String jsonString = operatedData.toString();
            synchronized (operatedPub) {
                operatedPub.setData(jsonString);
                operatedPub.notifyAll();
            }
        }).on("operated-delta", args -> {
            logger.fine("operated delta received");
            logger.fine(args[0].toString());
            JSONObject patch = (JSONObject) args[0];
            try {
                if (!Objects.equals(operatedData.get("to_tx"), (patch.get("from_tx")))) {
                    logger.fine("received tx ahead of current tx, retrieving data again");
                    //socket.emit("");
                    return;
                }
                operatedData.put("data", JSONPatcher.patch(operatedData.get("data"), patch.get("delta_data")));
                operatedData.put("to_tx", patch.get("to_tx"));
                String jsonString = operatedData.toString();
                synchronized (operatedPub) {
                    operatedPub.setData(jsonString);
                    operatedPub.notifyAll();
                }

            } catch (JSONException | JSONPatchException e) {
                e.printStackTrace();
            }
        }).on("open-book", args -> {
            logger.fine("open book data received");
            logger.fine(args[0].toString());
            try {
                JSONObject data = (JSONObject) args[0];
                String stockId = data.getString("stock_id");
                openBookData.put(stockId, data);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(stockId, new JSONObject());
                jsonObject.getJSONObject(stockId).put("buy", openBookData.getJSONObject(stockId).getJSONObject("data").get("1"));
                jsonObject.getJSONObject(stockId).put("sell", openBookData.getJSONObject(stockId).getJSONObject("data").get("2"));
                String jsonString = jsonObject.toString();
                synchronized (openBookPub) {
                    openBookPub.setData(jsonString);
                    openBookPub.notifyAll();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).on("open-book-delta", args -> {
            logger.fine("open book delta received");
            logger.fine(args[0].toString());
            try {
                JSONObject patch = (JSONObject) args[0];
                String stockId = patch .getString("stock_id");
                if (!openBookData.has(stockId) ||
                        !Objects.equals(
                                openBookData.getJSONObject(stockId).get("to_tx"),
                                patch.get("from_tx"))) {
                    logger.warning("open book received tx ahead of current tx or new stock id, retrieving data again");
                    socket.emit("open-book", new JSONObject().put("stockId", stockId));
                    return;
                }
                JSONObject stockData = openBookData.getJSONObject(stockId);
                stockData.put("data", JSONPatcher.patch(stockData.getJSONObject("data"), patch.get("delta_data")));
                openBookData.getJSONObject(stockId).put("to_tx", patch.get("to_tx"));

                JSONObject jsonObject = new JSONObject();
                jsonObject.put(stockId, new JSONObject());
                jsonObject.getJSONObject(stockId).put("buy", stockData.getJSONObject("data").get("1"));
                jsonObject.getJSONObject(stockId).put("sell", stockData.getJSONObject("data").get("2"));
                String jsonString = jsonObject.toString();

                synchronized (openBookPub) {
                    openBookPub.setData(jsonString);
                    openBookPub.notifyAll();
                }

            } catch (JSONException | JSONPatchException e) {
                e.printStackTrace();
            }
        }).on("historical-book", args -> {
            logger.fine("historical book data received");
            logger.fine(args[0].toString());
            try {
                JSONObject data = (JSONObject) args[0];
                String stockId = data.getString("stock_id");
                historicalBookData.put(stockId, data);
                JSONObject jsonObject = new JSONObject().put(stockId, data.get("data"));
                String jsonString = jsonObject.toString();
                synchronized (historicalBookPub) {
                    historicalBookPub.setData(jsonString);
                    historicalBookPub.notifyAll();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).on("historical-book-delta", args -> {
            logger.fine("historical book delta received");
            logger.fine(args[0].toString());
            try {
                JSONObject patch = (JSONObject) args[0];
                String stockId = patch.getString("stock_id");
                if (!historicalBookData.has(stockId) ||
                        !Objects.equals(
                                historicalBookData.getJSONObject(stockId).get("to_tx"),
                                patch.get("from_tx"))) {
                    logger.warning("historical book received tx ahead of current tx or new stock id, retrieving data again");
                    socket.emit("historical-book", new JSONObject().put("stockId", stockId));
                    return;
                }
                JSONObject stockData = historicalBookData.getJSONObject(stockId);
                stockData.put("data", JSONPatcher.patch(stockData.get("data"), patch.get("delta_data")));
                historicalBookData.getJSONObject(stockId).put("to_tx", patch.get("to_tx"));

                JSONObject jsonObject = new JSONObject().put(stockId, stockData.get("data"));
                String jsonString = jsonObject.toString();
                synchronized (historicalBookPub) {
                    historicalBookPub.setData(jsonString);
                    historicalBookPub.notifyAll();
                }

            } catch (JSONException | JSONPatchException e) {
                e.printStackTrace();
            }
        }).on("candles", args -> {
            logger.fine("candles data received");
            logger.fine(args[0].toString());
            JSONObject data = (JSONObject) args[0];
            try {
                String stockId = data.getString("stock_id");
                candlesData.put(stockId, data);
                JSONObject jsonObject = new JSONObject().put(stockId, new JSONObject());
                if (candlesData.getJSONObject(stockId).has("1")) {
                    jsonObject.put("buy", candlesData.getJSONObject(stockId).get("1"));
                }
                if (candlesData.getJSONObject(stockId).has("2")) {
                    jsonObject.put("sell", candlesData.getJSONObject(stockId).get("2"));
                }
                String jsonString = jsonObject.toString();
                synchronized (candlePub) {
                    candlePub.setData(jsonString);
                    candlePub.notifyAll();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).on("candles-delta", args -> {
            logger.fine("candles delta received");
            logger.fine(args[0].toString());
            try {
                JSONObject patch = (JSONObject) args[0];
                String stockId = patch.getString("stock_id");
                if (!candlesData.has(stockId) ||
                        !Objects.equals(
                                candlesData.getJSONObject(stockId).get("to_tx"),
                                patch.get("from_tx"))) {
                    logger.fine("received tx ahead of current tx or new stock id, retrieving data again");
                    socket.emit("historical-book", new JSONObject().put("stockId", stockId));
                    return;
                }
                JSONObject stockData = candlesData.getJSONObject(stockId);
                stockData.put("data", JSONPatcher.patch(stockData.getJSONObject("data"), patch.get("delta_data")));
                candlesData.getJSONObject(stockId).put("to_tx",patch.get("to_tx"));

                JSONObject jsonObject = new JSONObject().put(stockId, new JSONObject());
                if (candlesData.getJSONObject(stockId).has("1")) {
                    jsonObject.put("buy", candlesData.getJSONObject(stockId).get("1"));
                }
                if (candlesData.getJSONObject(stockId).has("2")) {
                    jsonObject.put("sell", candlesData.getJSONObject(stockId).get("2"));
                }
                String jsonString = jsonObject.toString();
                synchronized (candlePub) {
                    candlePub.setData(jsonString);
                    candlePub.notifyAll();
                }
            } catch (JSONException | JSONPatchException e) {
                e.printStackTrace();
            }
        }).on("board", args -> {
            logger.fine("board data received");
            logger.fine(args[0].toString());
            tickerData = (JSONObject) args[0];
            String jsonString = tickerData.toString();
            synchronized (tickerPub) {
                tickerPub.setData(jsonString);
                tickerPub.notifyAll();
            }
        }).on("board-delta", args -> {
            logger.fine("board delta received");
            logger.fine(args[0].toString());
            JSONObject patch = (JSONObject) args[0];
            try {
                if (!Objects.equals(tickerData.get("to_tx"), (patch.get("from_tx")))) {
                    logger.fine("received tx ahead of current tx, retrieving data again");
                    socket.emit("board");
                    return;
                }
                tickerData.put("data", JSONPatcher.patch(tickerData.get("data"), patch.get("delta_data")));
                tickerData.put("to_tx", patch.get("to_tx"));

                String jsonString = tickerData.toString();
                synchronized (tickerPub) {
                    tickerPub.setData(jsonString);
                    tickerPub.notifyAll();
                }

            } catch (JSONException | JSONPatchException e) {
                e.printStackTrace();
            }
        });
        socket.connect();
    }

    private JSONObject balanceDataToSend() throws JSONException {
        JSONObject balanceD = balanceData.getJSONObject("data");
        JSONObject dataToSend = new JSONObject(balanceD.toString());
        for (Iterator<String> it = balanceD.keys(); it.hasNext(); ) {
            String key = it.next();
            try {
                String currency = balanceD.getJSONObject(key).getString("currency");
                JSONObject wallet = dataToSend.getJSONObject(key);
                JSONObject currencyData = ((JSONObject)currenciesData.get("data")).getJSONObject(currency);
                wallet.put("currency_kind", currencyData.get("kind"));
                wallet.put("currency_name", currencyData.get("name"));
                wallet.put("currency_big_name", currencyData.get("big_name"));
                wallet.put("currency_prefix", currencyData.get("prefix"));
                wallet.put("currency_postfix", currencyData.get("postfix"));
                wallet.put("currency_decimals", currencyData.get("decimals"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataToSend;
    }


    public void subscribe(String[] marketPairs){
        for ( String marketPair : marketPairs ){
            socket.emit("subscribe", marketPair);
        }
    }

    public void unsubscribe(String[] marketPairs){
        for ( String marketPair : marketPairs ){
            socket.emit("unsubscribe", marketPair);
        }
    }

    @Override
    public void subscribe(String marketPair) {
        socket.emit("subscribe", marketPair);
    }

    @Override
    public void subscribe(List<String> marketPairs) {
        for ( String marketPair : marketPairs ){
            socket.emit("subscribe", marketPair);
        }
    }

    @Override
    public void unsubscribe(String marketPair) {
        socket.emit("unsubscribe", marketPair);
    }

    @Override
    public void unsubscribe(List<String> marketPairs) {
        for ( String marketPair : marketPairs ){
            socket.emit("unsubscribe", marketPair);
        }
    }

    @Override
    public void onCurrencies(Listener listener) {
        Subscriber subscriber = new Subscriber(listener, currenciesPub);
        subscriber.start();
    }

    @Override
    public void onBalance(Listener listener) {
        Subscriber subscriber = new Subscriber(listener, balancePub);
        subscriber.start();
    }

    @Override
    public void onOpenOrders(Listener listener) {
        Subscriber subscriber = new Subscriber(listener, openOrdersPub);
        subscriber.start();
    }

    @Override
    public void onHistoricalOrders(Listener listener) {
        Subscriber subscriber = new Subscriber(listener, historicalOrdersPub);
        subscriber.start();
    }

    @Override
    public void onOperated(Listener listener) {
        Subscriber subscriber = new Subscriber(listener, operatedPub);
        subscriber.start();
    }

    @Override
    public void onOpenBook(Listener listener) {
        Subscriber subscriber = new Subscriber(listener, openBookPub);
        subscriber.start();
    }

    @Override
    public void onHistoricalBook(Listener listener) {
        Subscriber subscriber = new Subscriber(listener, historicalBookPub);
        subscriber.start();
    }

    @Override
    public void onCandles(Listener listener) {
        Subscriber subscriber = new Subscriber(listener, candlePub);
        subscriber.start();
    }

    @Override
    public void onTicker(Listener listener) {
        Subscriber subscriber = new Subscriber(listener, tickerPub);
        subscriber.start();
    }
}
