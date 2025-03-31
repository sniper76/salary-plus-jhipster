import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUserShops } from 'app/modules/order/order.reducer';
import { getShopSaleList } from './sales.reducer';

export const Sales = () => {
  const dispatch = useAppDispatch();
  const shops = useAppSelector(state => state.orders.shops);
  const salesList = useAppSelector(state => state.sales.salesList);

  const dateNow = new Date();
  const today = dateNow.toISOString().slice(0, 10);

  dateNow.setDate(dateNow.getDate() - 7); // 7일 전으로 설정
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
      dispatch(getShopSaleList({ shopId: shops[0].id, startDate, endDate }));
    }
  }, [shops]);

  return (
    <Row>
      <Col md="12">
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
          {salesList?.map((item, idx) => (
            <Col key={idx} xs="auto" className="border text-center p-3">
              <p>{item.date}</p>
              <p>
                <Translate contentKey="global.menu.sales">매출</Translate>: {item.orderDetailPrice}
              </p>
              <p>
                <Translate contentKey="salesItemDiscount.home.title">할인</Translate>: {item.orderDetailDiscountPrice || 0}
              </p>
              <p>
                <Translate contentKey="global.label.refund">환불</Translate>: {item.shopRefundPrice || 0}
              </p>
              <Link to={`/sales/${item.shopId}/${item.date}`} className="btn btn-primary jh-create-entity">
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

export default Sales;
