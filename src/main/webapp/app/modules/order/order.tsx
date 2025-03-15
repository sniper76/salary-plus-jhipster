import React, { useEffect, useState } from 'react';
import { Translate } from 'react-jhipster';
import { Button, Col, Row } from 'reactstrap';
import './order.scss';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import {
  getShopModels,
  getShopTables,
  getShopOrders,
  getShopSalesItems,
  getUserShops,
  createOrder,
  updateOrder,
  updateOrderPaid,
  getShopOrderDetails,
  clearOrderDetails,
} from 'app/modules/order/order.reducer';
import OrderModal from 'app/modules/order/order-modal';

export const Order = () => {
  const orders = useAppSelector(state => state.orders.orders);
  const orderDetails = useAppSelector(state => state.orders.orderDetails);
  const shops = useAppSelector(state => state.orders.shops);
  const salesItems = useAppSelector(state => state.orders.salesItems);
  const models = useAppSelector(state => state.orders.models);
  const tables = useAppSelector(state => state.orders.tables);
  const dispatch = useAppDispatch();
  const [orderError, setOrderError] = useState(false);
  const dateNow = new Date();
  const today = dateNow.toISOString().slice(0, 10);
  const [currentDate, setCurrentDate] = useState(today);
  const [showModal, setShowModal] = useState(false);
  const [currentOrderId, setCurrentOrderId] = useState(null);
  const [currentTableId, setCurrentTableId] = useState(null);

  const handleClose = () => {
    setShowModal(false);
    setCurrentOrderId(null);
    setCurrentTableId(null);
    dispatch(clearOrderDetails());
  };

  const handleOpen = () => {
    setShowModal(true);
  };

  const handleOrder = obj => {
    obj.shopId = selectedValue;
    obj.date = currentDate;
    if (obj.orderId == null) {
      console.warn('createOrder', obj);
      dispatch(createOrder(obj));
    } else {
      console.warn('updateOrder', obj);
      dispatch(updateOrder(obj));
    }
  };

  const [selectedValue, setSelectedValue] = useState(-1);

  const handleSelect = e => {
    setSelectedValue(e.target.value);
  };

  const handlePaidClick = e => () => {
    dispatch(updateOrderPaid({ shopId: selectedValue, date: currentDate, orderId: e.orderId }));
  };

  const handleChangeClick = e => () => {
    console.warn('handleChangeClick', e);
    setCurrentOrderId(e.orderId);
    setCurrentTableId(e.shopTableId);
    handleOpen();
    dispatch(getShopOrderDetails({ shopId: selectedValue, date: currentDate, orderId: e.orderId }));
  };

  useEffect(() => {
    dispatch(getUserShops());
  }, []);

  useEffect(() => {
    if (shops.length > 0) {
      setSelectedValue(shops[0].id);
      dispatch(getShopOrders({ shopId: shops[0].id, date: today }));
      dispatch(getShopSalesItems({ shopId: shops[0].id }));
      dispatch(getShopModels({ shopId: shops[0].id }));
      dispatch(getShopTables({ shopId: shops[0].id }));
    }
  }, [shops]);

  return (
    <Row>
      <Col md="12">
        <Row>
          <div className="custom-date-input">
            <select onChange={handleSelect} value={selectedValue}>
              {shops.map(shop => (
                <option value={shop.id} key={shop.id}>
                  {shop.nameKo}
                </option>
              ))}
            </select>
            <input defaultValue={currentDate} type="date" />
            <Button onClick={handleOpen} className="alert-link">
              <Translate contentKey="entity.action.open">Open</Translate>
            </Button>
          </div>
          <OrderModal
            salesItems={salesItems}
            models={models}
            tables={tables}
            orderId={currentOrderId}
            tableId={currentTableId}
            orderDetails={orderDetails}
            showModal={showModal}
            handleOrder={handleOrder}
            handleClose={handleClose}
            orderError={orderError}
          />
        </Row>
        <Row>
          {orders.map((item, idx) => (
            <Col key={idx} xs="auto" className="border text-center p-3">
              <p>{item.tableNo}</p>
              <p>{item.totalPrice}</p>
              {item.paid ? (
                <p>
                  <b>
                    <Translate contentKey="order.button.paid">Payment Processed</Translate>
                  </b>
                </p>
              ) : (
                <>
                  <Button color="secondary" onClick={handleChangeClick(item)}>
                    <Translate contentKey="order.button.order_change">Order Change</Translate>
                  </Button>
                  <Button color="primary" onClick={handlePaidClick(item)}>
                    <Translate contentKey="order.button.pay_processing">Payment Processing</Translate>
                  </Button>
                </>
              )}
            </Col>
          ))}
        </Row>
      </Col>
    </Row>
  );
};

export default Order;
