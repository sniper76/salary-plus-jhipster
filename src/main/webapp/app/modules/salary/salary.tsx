import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, Card, CardBody, CardTitle, CardText, Input, Label } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUserShops } from 'app/modules/order/order.reducer';
import { getShopDailySalaryList, getShopSalaryForUser } from './salary.reducer';
import SalaryDetailModal from 'app/modules/salary/salary-detail-modal';

export const Salary = () => {
  const dispatch = useAppDispatch();
  const shops = useAppSelector(state => state.orders.shops);
  const salaryList = useAppSelector(state => state.salaries.salaryList);

  const dateNow = new Date();
  const today = dateNow.toISOString().slice(0, 10);
  const dateGap = 6;

  dateNow.setDate(dateNow.getDate() - dateGap);
  const sevenDaysAgo = dateNow.toISOString().slice(0, 10);

  const [selectedShopId, setSelectedShopId] = useState(-1);
  const [startDate, setStartDate] = useState(sevenDaysAgo);
  const [endDate, setEndDate] = useState(today);
  const [durationWeeks, setDurationWeeks] = useState(1); // 1주 or 2주
  const [showModal, setShowModal] = useState(false);

  const handleClose = () => {
    setShowModal(false);
  };

  const handleOpen = item => () => {
    setShowModal(true);
    dispatch(getShopSalaryForUser({ id: selectedShopId, userId: item.userId, startDate, endDate }));
    // handleReset();
  };

  const handleEndDateChange = e => {
    const newEndDate = e.target.value;
    setEndDate(newEndDate);

    const end = new Date(newEndDate);
    end.setDate(end.getDate() - dateGap * durationWeeks);
    setStartDate(end.toISOString().slice(0, 10));
  };

  const handleDurationChange = e => {
    const weeks = Number(e.target.value);
    setDurationWeeks(weeks);

    const end = new Date(endDate);
    end.setDate(end.getDate() - dateGap * weeks);
    setStartDate(end.toISOString().slice(0, 10));
  };

  useEffect(() => {
    dispatch(getUserShops());
  }, []);

  useEffect(() => {
    if (shops.length > 0) {
      setSelectedShopId(shops[0].id);
      dispatch(getShopDailySalaryList({ shopId: shops[0].id, startDate, endDate }));
    }
  }, [shops, selectedShopId, startDate, endDate]);

  const salaryDetails = useAppSelector(state => state.salaries.salaryForUser);

  return (
    <div className="p-4">
      <h2 className="mb-4">
        <Translate contentKey="global.menu.salary">주급</Translate>
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

        <Col md="2">
          <Label className="d-block">
            <Translate contentKey="global.label.duration">기간</Translate>
          </Label>
          <div className="form-check form-check-inline">
            <Input
              className="form-check-input"
              type="radio"
              id="oneWeek"
              name="duration"
              value={1}
              checked={durationWeeks === 1}
              onChange={handleDurationChange}
            />
            <Label className="form-check-label" for="oneWeek">
              <Translate contentKey="global.label.oneWeek">1주</Translate>
            </Label>
          </div>
          <div className="form-check form-check-inline">
            <Input
              className="form-check-input"
              type="radio"
              id="twoWeeks"
              name="duration"
              value={2}
              checked={durationWeeks === 2}
              onChange={handleDurationChange}
            />
            <Label className="form-check-label" for="twoWeeks">
              <Translate contentKey="global.label.twoWeeks">2주</Translate>
            </Label>
          </div>
        </Col>

        <Col md="3">
          <Label for="startDateSelect">
            <Translate contentKey="global.label.startDateSelect">시작일 선택</Translate>
          </Label>
          <Input id="startDateSelect" type="date" value={startDate} disabled />
        </Col>

        <Col md="3">
          <Label for="endDateSelect">
            <Translate contentKey="global.label.endDateSelect">종료일 선택</Translate>
          </Label>
          <Input id="endDateSelect" type="date" value={endDate} onChange={handleEndDateChange} />
        </Col>
      </Row>

      <Row>
        {salaryList.map(item => (
          <Col key={item.userId} xs="6" sm="4" md="3" lg="2" className="mb-3">
            <Card className="h-100 border shadow-sm small">
              <CardBody className="p-2" style={{ fontSize: '1rem' }}>
                <CardTitle tag="h5" className="fw-bold text-center mb-1">
                  {item.lastName} {item.firstName}
                </CardTitle>
                <CardText className="text-muted text-center mb-2">{item.modelNo}</CardText>

                <div className="d-flex justify-content-between mb-1">
                  <span>
                    <Translate contentKey="global.menu.salary">주급</Translate>
                  </span>
                  <span className="fw-bold">{item.salaryPrice.toLocaleString()}</span>
                </div>
                <div className="d-flex justify-content-between mb-1">
                  <span>
                    <Translate contentKey="global.label.dayPenaltyPrice">결근 벌금</Translate>
                  </span>
                  <span className="text-danger">{item.dayPenaltyPrice.toLocaleString()}</span>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>
                    <Translate contentKey="global.label.timePenaltyPrice">지각 벌금</Translate>
                  </span>
                  <span className="text-warning">{item.timePenaltyPrice.toLocaleString()}</span>
                </div>

                <div className="text-center">
                  <Button color="primary" size="sm" className="w-100" onClick={handleOpen(item)}>
                    <Translate contentKey="global.menu.salary">주급</Translate>{' '}
                    <Translate contentKey="global.label.details">상세</Translate>
                  </Button>
                </div>
              </CardBody>
            </Card>
          </Col>
        ))}
      </Row>
      <SalaryDetailModal salaryDetails={salaryDetails} showModal={showModal} handleClose={handleClose} />
    </div>
  );
};

export default Salary;
