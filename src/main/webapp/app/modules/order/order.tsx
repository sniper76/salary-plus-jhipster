import React, { useEffect, useState } from 'react';
import { Translate } from 'react-jhipster';
import { Button, Col, Row, Label, Input } from 'reactstrap';
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
  const [selectedShopId, setSelectedShopId] = useState(-1);
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
    obj.shopId = selectedShopId;
    obj.date = currentDate;
    console.warn('handleOrder', obj);
    if (obj.orderId == null) {
      dispatch(createOrder(obj)).then(() => {
        dispatch(getShopOrders({ shopId: selectedShopId, date: currentDate }));
      });
    } else {
      dispatch(updateOrder(obj)).then(() => {
        dispatch(getShopOrders({ shopId: selectedShopId, date: currentDate }));
      });
    }
  };

  const handlePayOrder = e => {
    e.shopId = selectedShopId;
    e.date = currentDate;
    // console.warn('e', e);
    dispatch(createOrderWithDiscounts(e));
  };

  const handleRefundOrder = e => {
    e.shopId = selectedShopId;
    e.date = currentDate;
    // console.warn('e', e);
    dispatch(updateOrderRefund(e));
  };

  const handlePaidClick = e => () => {
    // dispatch(updateOrderPaid({ shopId: selectedValue, date: currentDate, orderId: e.orderId }));
    setCurrentOrderId(e.orderId);
    setCurrentTableId(e.shopTableId);
    dispatch(getShopOrderDetailWithDiscounts({ shopId: selectedShopId, date: currentDate, orderId: e.orderId }));
    dispatch(getShopOrderDetails({ shopId: selectedShopId, date: currentDate, orderId: e.orderId })).then(() => {
      setShowPayModal(true);
    });
  };

  const handleRefundClick = e => () => {
    // dispatch(updateOrderPaid({ shopId: selectedValue, date: currentDate, orderId: e.orderId }));
    setCurrentOrderId(e.orderId);
    setCurrentTableId(e.shopTableId);
    dispatch(getShopOrderDetails({ shopId: selectedShopId, date: currentDate, orderId: e.orderId })).then(() => {
      setShowRefundModal(true);
    });
  };

  const handleChangeClick = e => () => {
    // console.warn('handleChangeClick', e, currentDate);
    // handleOpen();
    setCurrentOrderId(e.orderId);
    setCurrentTableId(e.shopTableId);
    // dispatch(getShopOrderDetails({ shopId: selectedShopId, date: currentDate, orderId: e.orderId }));
    dispatch(getShopOrderDetails({ shopId: selectedShopId, date: currentDate, orderId: e.orderId })).then(() => {
      setShowModal(true); // 주문 정보를 가져온 후에 모달을 열도록 설정
    });
  };

  useEffect(() => {
    dispatch(getUserShops());
  }, []);

  useEffect(() => {
    if (shops.length > 0) {
      setSelectedShopId(shops[0].id);
      dispatch(getShopSalesItems({ shopId: shops[0].id }));
      dispatch(getShopModels({ shopId: shops[0].id }));
      dispatch(getShopTables({ shopId: shops[0].id }));
      dispatch(getShopOrders({ shopId: shops[0].id, date: currentDate }));
    }
  }, [shops]);

  useEffect(() => {
    dispatch(getShopOrders({ shopId: selectedShopId, date: currentDate }));
  }, [currentDate]);

  return (
    <div className="p-4">
      <h2 className="mb-4">
        <Translate contentKey="global.menu.order">주문</Translate>
      </h2>

      <Row className="align-items-end mb-4">
        <Col md="4">
          <Label for="shopSelect">
            <Translate contentKey="global.label.shopSelect">상점 선택</Translate>
          </Label>
          <Input type="select" id="shopSelect" value={selectedShopId} onChange={e => setSelectedShopId(Number(e.target.value))}>
            {shops.map(shop => (
              <option key={shop.id} value={shop.id}>
                {shop.nameKo}
              </option>
            ))}
          </Input>
        </Col>
        <Col md="3">
          <Label for="dateSelect">
            <Translate contentKey="global.label.dateSelect">날짜 선택</Translate>
          </Label>
          <Input type="date" id="dateSelect" value={currentDate} onChange={e => setCurrentDate(e.target.value)} />
        </Col>
        <Col md="5" className="d-flex justify-content-end gap-2">
          <Button color="info" onClick={handleOpen}>
            <Translate contentKey="entity.action.open">주문창 열기</Translate>
          </Button>
        </Col>
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

      <Row className="g-4">
        {orders.map((item, idx) => (
          <Col key={idx} xs="6" sm="4" md="3" lg="2" className="mb-3">
            <div className="border rounded shadow-sm p-3 h-100 d-flex flex-column justify-content-between">
              <div>
                <h5 className="text-success fw-bold">{item.tableNo}</h5>
                <div className="mb-1 d-flex justify-content-between">
                  <span>
                    <Translate contentKey="order.label.totalPrice">총액</Translate>
                  </span>
                  <span>{item.totalPrice}</span>
                </div>

                {item.paid ? (
                  <>
                    {item.discountResponseList.map((elem, index) => (
                      <div className="mb-1 d-flex justify-content-between" key={index}>
                        <span>{elem.discountNameKo}</span>
                        <span>{elem.discountPrice}</span>
                      </div>
                    ))}

                    {item.refundResponse == null ? (
                      <div className="d-flex flex-column gap-2 mt-2">
                        <Button color="secondary" onClick={handleRefundClick(item)}>
                          <Translate contentKey="order.button.refund">환불하기</Translate>
                        </Button>
                      </div>
                    ) : (
                      <>
                        <div className="mb-1 d-flex justify-content-between">
                          <span>
                            <Translate contentKey="order.label.shopPrice">상점환불금액</Translate>
                          </span>
                          <span>{item.refundResponse.shopPrice}</span>
                        </div>
                        <div className="mb-1 d-flex justify-content-between">
                          <span>
                            <Translate contentKey="order.label.modelPrice">모델환불금액</Translate>
                          </span>
                          <span>{item.refundResponse.modelPrice}</span>
                        </div>
                        <div className="mb-1 d-flex justify-content-between">
                          <span>
                            <Translate contentKey="order.label.mamaPrice">마마환불금액</Translate>
                          </span>
                          <span>{item.refundResponse.mamaPrice}</span>
                        </div>
                      </>
                    )}
                  </>
                ) : (
                  <div className="d-flex flex-column gap-2 mt-2">
                    <Button color="secondary" onClick={handleChangeClick(item)}>
                      <Translate contentKey="order.button.order_change">주문변경</Translate>
                    </Button>
                    <Button color="primary" onClick={handlePaidClick(item)}>
                      <Translate contentKey="order.button.pay_processing">계산하기</Translate>
                    </Button>
                  </div>
                )}
                <div className="d-flex flex-column gap-2 mt-2">
                  <Button color="success" disabled>
                    <Translate contentKey="order.button.paid">계산완료</Translate>
                  </Button>
                </div>
              </div>
            </div>
          </Col>
        ))}
      </Row>
    </div>
  );
};

export default Order;
