import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Container, Button, Col, Row } from 'reactstrap';
import './order.scss';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUserRoles } from 'app/modules/order/order.reducer';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBath, faBathtub, faBed, faTicketSimple, faWineGlass } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import OrderModal from 'app/modules/order/order-modal';

export const Order = () => {
  const account = useAppSelector(state => state.authentication.account);
  const order = useAppSelector(state => state.orders.order);
  const dispatch = useAppDispatch();
  const [orderError, setOrderError] = useState(false);
  const dateNow = new Date();
  const today = dateNow.toISOString().slice(0, 10);
  const [currentDate, setCurrentDate] = useState(today);

  useEffect(() => {
    dispatch(getUserRoles());
  }, []);

  const btnOnClick = () => {
    console.error('Order', order, account);
  };
  const [showModal, setShowModal] = useState(false);

  const handleClose = () => {
    setShowModal(false);
  };

  const handleOpen = () => {
    setShowModal(true);
  };

  const handleOrder = () => {
    console.error('Order', order, account);
  };

  return (
    <Container>
      <div className="custom-date-input">
        <input defaultValue={currentDate} type="date" />
        <Button onClick={handleOpen} className="alert-link">
          <Translate contentKey="entity.action.open">Open</Translate>
        </Button>
      </div>
      <OrderModal showModal={showModal} handleOrder={handleOrder} handleClose={handleClose} orderError={orderError} />
    </Container>
  );
};

export default Order;
