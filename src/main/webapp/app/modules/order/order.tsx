import React, { useEffect, useState } from 'react';
import { Translate } from 'react-jhipster';
import { Button, Col, Row } from 'reactstrap';
import './order.scss';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getShopModels, getShopOrders, getShopSalesItems, getUserShops } from 'app/modules/order/order.reducer';
import OrderModal from 'app/modules/order/order-modal';

export const Order = () => {
  const account = useAppSelector(state => state.authentication.account);
  const orders = useAppSelector(state => state.orders.orders);
  const shops = useAppSelector(state => state.orders.shops);
  const salesItems = useAppSelector(state => state.orders.salesItems);
  const models = useAppSelector(state => state.orders.models);
  const dispatch = useAppDispatch();
  const [orderError, setOrderError] = useState(false);
  const dateNow = new Date();
  const today = dateNow.toISOString().slice(0, 10);
  const [currentDate, setCurrentDate] = useState(today);

  const [showModal, setShowModal] = useState(false);

  const handleClose = () => {
    setShowModal(false);
  };

  const handleOpen = () => {
    setShowModal(true);
  };

  const handleOrder = obj => {
    console.error('parent Orders', orders, obj);
  };

  const [selectedValue, setSelectedValue] = useState(-1);

  const handleSelect = e => {
    setSelectedValue(e.target.value);
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
            orderId={null}
            showModal={showModal}
            handleOrder={handleOrder}
            handleClose={handleClose}
            orderError={orderError}
          />
        </Row>
        <Row>
          {orders.map((item, idx) => (
            <Col key={idx} xs="auto" className="border text-center p-3">
              {item.no}
            </Col>
          ))}
        </Row>
      </Col>
    </Row>
  );
};

export default Order;
