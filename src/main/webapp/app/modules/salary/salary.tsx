import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUserShops } from 'app/modules/order/order.reducer';
import { getShopDailySalaryList } from './salary.reducer';

export const Salary = () => {
  const dispatch = useAppDispatch();
  const shops = useAppSelector(state => state.orders.shops);
  const salaryList = useAppSelector(state => state.salaries.salaryList);

  const dateNow = new Date();
  const today = dateNow.toISOString().slice(0, 10);

  dateNow.setDate(dateNow.getDate() - 7);
  const sevenDaysAgo = dateNow.toISOString().slice(0, 10);

  const [selectedValue, setSelectedValue] = useState(-1);
  const [startDate, setStartDate] = useState(sevenDaysAgo);
  const [endDate, setEndDate] = useState(today);

  const handleSelect = e => {
    setSelectedValue(e.target.value);
  };

  const handleStartDateChange = e => {
    setStartDate(e.target.value);
  };

  const handleEndDateChange = e => {
    setEndDate(e.target.value);
  };

  useEffect(() => {
    dispatch(getUserShops());
  }, []);

  useEffect(() => {
    if (shops.length > 0) {
      setSelectedValue(shops[0].id);
      dispatch(getShopDailySalaryList({ shopId: shops[0].id, startDate, endDate }));
    }
  }, [shops, startDate, endDate]);

  return (
    <Row>
      <Col md="12">
        <h2>
          <Translate contentKey="global.menu.salary">주급</Translate>
        </h2>
        <Row>
          <div className="custom-date-input-select">
            <select onChange={handleSelect} value={selectedValue}>
              {shops.map(shop => (
                <option value={shop.id} key={shop.id}>
                  {shop.nameKo}
                </option>
              ))}
            </select>
            <input defaultValue={startDate} type="date" onChange={handleStartDateChange} />
            <input defaultValue={endDate} type="date" onChange={handleEndDateChange} />
          </div>
        </Row>
        <Row>
          {salaryList?.map((item, idx) => (
            <Col key={idx} xs="auto" className="border text-center p-3">
              <p>
                {item.firstName} {item.lastName}
              </p>
              <p>{item.modelNo}</p>
              <p>
                <Translate contentKey="global.menu.salary">주급</Translate>: {item.salaryPrice}
              </p>
              <p>
                <Translate contentKey="penalty.home.title">벌금</Translate>: {item.dayPenaltyPrice || 0}
              </p>
              <p>
                <Translate contentKey="penalty.home.title">벌금</Translate>: {item.timePenaltyPrice || 0}
              </p>
              <Link to={`/salary/${item.price}/${item.modelNo}`} className="btn btn-primary jh-create-entity">
                <span className="d-md-inline">
                  <Translate contentKey="order.label.details">상세</Translate>
                </span>
              </Link>
            </Col>
          ))}
        </Row>
      </Col>
    </Row>
  );
};

export default Salary;
