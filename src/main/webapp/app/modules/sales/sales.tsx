import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUserShops } from 'app/modules/order/order.reducer';
import { getPageShopSales } from './sales.reducer';

import './sales.scss';

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
      dispatch(getPageShopSales({ shopId: shops[0].id, startDate, endDate }));
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
            <input defaultValue={startDate} type="date" onChange={handleStartDateChange} />
            <input defaultValue={endDate} type="date" onChange={handleEndDateChange} />
          </div>
        </Row>
        <Row>
          {salesList?.map((item, idx) => (
            <Col key={idx} xs="auto" className="border text-center p-3">
              <p>{item.date}</p>
              <p>총 매출: {item.orderDetailPrice}</p>
              <p>총 할인: {item.orderDetailDiscountPrice || 0}</p>
              <p>환불: {item.shopRefundPrice || 0}</p>
              <Link to={`/sales/${item.shopId}/${item.date}`}>
                <button>상세</button>
              </Link>
            </Col>
          ))}
        </Row>
      </Col>
    </Row>
  );
};

export default Sales;
