import React, { useEffect, useState } from 'react';
import { Col, Row, Button } from 'reactstrap';
import { Translate } from 'react-jhipster';
import './work.scss';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createCheckIn, createAllCheckIn, getShopUsers } from 'app/modules/work/work.reducer';
import { getUserShops } from 'app/modules/order/order.reducer';

export const Work = () => {
  const users = useAppSelector(state => state.works.users);
  const shops = useAppSelector(state => state.orders.shops);
  const dispatch = useAppDispatch();

  const dateNow = new Date();
  const today = dateNow.toISOString().slice(0, 10);
  const [currentDate, setCurrentDate] = useState(today);
  const [selectedValue, setSelectedValue] = useState(-1);

  const handleDateChange = e => {
    setCurrentDate(e.target.value);
  };

  const handleSelect = e => {
    setSelectedValue(e.target.value);
  };

  const handleCheckInClick = e => () => {
    // console.warn('handleCheckInClick', e);
    dispatch(createCheckIn({ shopId: shops[0].id, date: currentDate, userId: e.userId }));
  };

  const handleAllCheckInClick = () => {
    dispatch(createAllCheckIn({ shopId: shops[0].id, date: currentDate }));
  };

  useEffect(() => {
    dispatch(getUserShops());
  }, []);

  useEffect(() => {
    if (shops.length > 0) {
      setSelectedValue(shops[0].id);
      dispatch(getShopUsers({ shopId: shops[0].id, date: currentDate }));
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
            <input defaultValue={currentDate} type="date" onChange={handleDateChange} />
            <Button color="primary" onClick={handleAllCheckInClick}>
              <Translate contentKey="work.button.allCheckIn">전체출근</Translate>
            </Button>
          </div>
        </Row>
        <Row>
          {users.map((item, idx) => (
            <Col key={idx} xs="auto" className="border text-center p-3">
              <p>
                {item.lastName} {item.firstName}
              </p>
              <p>{item.modelNo}</p>
              <p>{item.checkIn}</p>
              <p>
                {item.checkIn ? (
                  <Button color="secondary">
                    <Translate contentKey="work.label.checkInOk">출근완료</Translate>
                  </Button>
                ) : (
                  <Button color="primary" onClick={handleCheckInClick(item)}>
                    <Translate contentKey="work.button.checkIn">출근하기</Translate>
                  </Button>
                )}
              </p>
            </Col>
          ))}
        </Row>
      </Col>
    </Row>
  );
};

export default Work;
