import React, { useEffect, useState } from 'react';
import { Col, Row, Label, Input, Button } from 'reactstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getShopOrderDetails, getUserShops } from 'app/modules/order/order.reducer';
import { getShopSaleList } from './sales.reducer';
import { Translate } from 'react-jhipster';
import SalesDetailModal from 'app/modules/sales/sales-detail-modal';

export const Sales = () => {
  const dispatch = useAppDispatch();
  const shops = useAppSelector(state => state.orders.shops);
  const salesList = useAppSelector(state => state.sales.salesList);

  const dateNow = new Date();
  const today = dateNow.toISOString().slice(0, 10);
  dateNow.setDate(dateNow.getDate() - 7);
  const sevenDaysAgo = dateNow.toISOString().slice(0, 10);

  const [selectedShopId, setSelectedShopId] = useState(-1);
  const [selectedDate, setSelectedDate] = useState(null);
  const [startDate, setStartDate] = useState(sevenDaysAgo);
  const [endDate, setEndDate] = useState(today);

  const handleStartDateChange = e => setStartDate(e.target.value);
  const handleEndDateChange = e => setEndDate(e.target.value);

  useEffect(() => {
    dispatch(getUserShops());
  }, []);

  useEffect(() => {
    if (shops.length > 0) {
      setSelectedShopId(shops[0].id);
      dispatch(getShopSaleList({ shopId: shops[0].id, startDate, endDate }));
    }
  }, [shops, startDate, endDate]);

  const calculatePrice = elem => {
    // console.warn('calculatePrice', elem);
    return elem.shopRefundPrice + elem.mamaRefundPrice + elem.modeRefundPrice;
  };
  const [showDetailModal, setShowDetailModal] = useState(false);

  const handleDetailClose = () => {
    setShowDetailModal(false);
  };

  const handleDetailClick = e => () => {
    setSelectedShopId(shops[0].id);
    setSelectedDate(e.date);
    setShowDetailModal(true);
  };

  return (
    <div className="p-4">
      <h2 className="mb-4">
        <Translate contentKey="global.menu.sales">매출</Translate>
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
          <Label for="startDateSelect">
            <Translate contentKey="global.label.startDateSelect">시작일</Translate>
          </Label>
          <Input id="startDateSelect" type="date" value={startDate} onChange={handleStartDateChange} />
        </Col>
        <Col md="3">
          <Label for="endDateSelect">
            <Translate contentKey="global.label.endDateSelect">종료일</Translate>
          </Label>
          <Input id="endDateSelect" type="date" value={endDate} onChange={handleEndDateChange} />
        </Col>
        <Col md="2"></Col>
        <SalesDetailModal
          shopId={selectedShopId}
          date={selectedDate}
          showDetailModal={showDetailModal}
          handleDetailClose={handleDetailClose}
        />
      </Row>

      {/* 매출 카드 영역 */}
      <Row className="g-4">
        {salesList?.map((item, idx) => (
          <Col key={idx} xs="6" sm="4" md="3" lg="2" className="mb-3">
            <div className="border rounded shadow-sm p-3 h-100 d-flex flex-column justify-content-between">
              <div>
                <h5 className="text-success fw-bold">{item.date}</h5>
                <div className="mb-1 d-flex justify-content-between">
                  <span>
                    <Translate contentKey="global.menu.sales">매출</Translate>
                  </span>
                  <span className="fw-bold">{item.orderDetailPrice}</span>
                </div>
                <div className="mb-1 d-flex justify-content-between">
                  <span>
                    <Translate contentKey="salesItemDiscount.home.title">할인</Translate>
                  </span>
                  <span>{item.orderDetailDiscountPrice || 0}</span>
                </div>
                <div className="mb-3 d-flex justify-content-between">
                  <span>
                    <Translate contentKey="global.label.refund">환불</Translate>
                  </span>
                  <span>{calculatePrice(item)}</span>
                </div>
              </div>
              <div className="d-flex flex-column gap-2 mt-2">
                <Button color="secondary" onClick={handleDetailClick(item)}>
                  <Translate contentKey="order.label.details">주문 상세</Translate>
                </Button>
              </div>
            </div>
          </Col>
        ))}
      </Row>
    </div>
  );
};

export default Sales;
