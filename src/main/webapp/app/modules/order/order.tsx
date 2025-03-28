import React, { useEffect, useState } from 'react';
import { Translate } from 'react-jhipster';
import { Button, Col, Row } from 'reactstrap';
import './order.scss';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import {
  clearOrderDetails,
  createOrder,
  createOrderWithDiscounts,
  getShopModels,
  getShopOrderDetails,
  getShopOrderDetailWithDiscounts,
  getShopOrders,
  getShopSalesItems,
  getShopTables,
  getUserShops,
  updateOrder,
  updateOrderPaid,
  updateOrderRefund,
} from 'app/modules/order/order.reducer';
import OrderModal from 'app/modules/order/order-modal';
import OrderModalDiscount from 'app/modules/order/order-discount-modal';
import OrderModalRefund from 'app/modules/order/order-refund-modal';

export const Order = () => {
  const orders = useAppSelector(state => state.orders.orders);
  const orderDetails = useAppSelector(state => state.orders.orderDetails);
  const orderDetailWithDiscounts = useAppSelector(state => state.orders.orderDetailWithDiscounts);
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
  const [selectedValue, setSelectedValue] = useState(-1);
  const [showPayModal, setShowPayModal] = useState(false);
  const [showRefundModal, setShowRefundModal] = useState(false);

  const handleClose = () => {
    setShowModal(false);
  };

  const handlePayClose = () => {
    setShowPayModal(false);
  };

  const handleRefundClose = () => {
    setShowRefundModal(false);
  };

  const handleOpen = () => {
    setShowModal(true);
    handleReset();
  };

  const handleReset = () => {
    setCurrentOrderId(null);
    setCurrentTableId(null);
    dispatch(clearOrderDetails());
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

  const handlePayOrder = e => {
    e.shopId = selectedValue;
    e.date = currentDate;
    console.warn('e', e);
    dispatch(createOrderWithDiscounts(e));
  };

  const handleRefundOrder = e => {
    e.shopId = selectedValue;
    e.date = currentDate;
    console.warn('e', e);
    dispatch(updateOrderRefund(e));
  };

  const handleSelect = e => {
    setSelectedValue(e.target.value);
  };

  const handlePaidClick = e => () => {
    // dispatch(updateOrderPaid({ shopId: selectedValue, date: currentDate, orderId: e.orderId }));
    setCurrentOrderId(e.orderId);
    setCurrentTableId(e.shopTableId);
    dispatch(getShopOrderDetailWithDiscounts({ shopId: selectedValue, date: currentDate, orderId: e.orderId }));
    dispatch(getShopOrderDetails({ shopId: selectedValue, date: currentDate, orderId: e.orderId })).then(() => {
      setShowPayModal(true);
    });
  };

  const handleRefundClick = e => () => {
    // dispatch(updateOrderPaid({ shopId: selectedValue, date: currentDate, orderId: e.orderId }));
    setCurrentOrderId(e.orderId);
    setCurrentTableId(e.shopTableId);
    dispatch(getShopOrderDetails({ shopId: selectedValue, date: currentDate, orderId: e.orderId })).then(() => {
      setShowRefundModal(true);
    });
  };

  const handleChangeClick = e => () => {
    // console.warn('handleChangeClick', e, currentDate);
    // handleOpen();
    setCurrentOrderId(e.orderId);
    setCurrentTableId(e.shopTableId);
    // dispatch(getShopOrderDetails({ shopId: selectedValue, date: currentDate, orderId: e.orderId }));
    dispatch(getShopOrderDetails({ shopId: selectedValue, date: currentDate, orderId: e.orderId })).then(() => {
      setShowModal(true); // 주문 정보를 가져온 후에 모달을 열도록 설정
    });
  };

  const handleDateChange = e => {
    console.warn('handleDateChange', e.target.value);
    setCurrentDate(e.target.value);
  };

  useEffect(() => {
    dispatch(getUserShops());
  }, []);

  useEffect(() => {
    if (shops.length > 0) {
      setSelectedValue(shops[0].id);
      dispatch(getShopSalesItems({ shopId: shops[0].id }));
      dispatch(getShopModels({ shopId: shops[0].id }));
      dispatch(getShopTables({ shopId: shops[0].id }));
      dispatch(getShopOrders({ shopId: shops[0].id, date: currentDate }));
    }
  }, [shops]);

  useEffect(() => {
    dispatch(getShopOrders({ shopId: selectedValue, date: currentDate }));
  }, [currentDate]);

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
            <input defaultValue={currentDate} type="date" onChange={handleDateChange} />
            <Button color="primary" onClick={handleOpen}>
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
          <OrderModalDiscount
            orderId={currentOrderId}
            orderDetails={orderDetails}
            orderDetailWithDiscounts={orderDetailWithDiscounts}
            showPayModal={showPayModal}
            handlePayOrder={handlePayOrder}
            handlePayClose={handlePayClose}
            orderError={orderError}
          />
          <OrderModalRefund
            orderId={currentOrderId}
            orderDetails={orderDetails}
            showRefundModal={showRefundModal}
            handleRefundOrder={handleRefundOrder}
            handleRefundClose={handleRefundClose}
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
                    {item.discountResponseList.map((elem, index) => (
                      <b key={index}>
                        {elem.discountNameKo}
                        {elem.discountPrice}
                      </b>
                    ))}
                  </b>
                  <b>
                    <Translate contentKey="order.button.paid">계산완료</Translate>
                  </b>
                  {item.refundResponse == null ? (
                    <Button color="secondary" onClick={handleRefundClick(item)}>
                      <Translate contentKey="order.button.refund">환불하기</Translate>
                    </Button>
                  ) : (
                    <>
                      <b>
                        <Translate contentKey="order.label.shopPrice">상점환불금액</Translate>
                        {item.refundResponse.shopPrice}
                      </b>
                      <b>
                        <Translate contentKey="order.label.modelPrice">모델환불금액</Translate>
                        {item.refundResponse.modelPrice}
                      </b>
                      <b>
                        <Translate contentKey="order.label.mamaPrice">마마환불금액</Translate>
                        {item.refundResponse.mamaPrice}
                      </b>
                    </>
                  )}
                </p>
              ) : (
                <>
                  <Button color="secondary" onClick={handleChangeClick(item)}>
                    <Translate contentKey="order.button.order_change">주문변경</Translate>
                  </Button>
                  <Button color="primary" onClick={handlePaidClick(item)}>
                    <Translate contentKey="order.button.pay_processing">계산하기</Translate>
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
