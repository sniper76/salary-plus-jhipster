import React, { useEffect, useState } from 'react';
import { Link, useParams, useLocation, useNavigate } from 'react-router-dom';
import { Badge, Button, Row, Col, Table } from 'reactstrap';
import { getPaginationState, JhiItemCount, JhiPagination, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getShopSaleDetailList, getShopSalesForPage, reset, resetSalesDetailList } from './sales.reducer';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons';

export const SalesDetail = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const { shopId, date } = useParams();

  const [pagination, setPagination] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const getSalesFromProps = () => {
    dispatch(
      getShopSalesForPage({
        id: parseInt(shopId, 10),
        query: date,
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: `${pagination.sort},${pagination.order}`,
      }),
    );
    const endURL = `?page=${pagination.activePage}&sort=${pagination.sort},${pagination.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    getSalesFromProps();
  }, [pagination.activePage, pagination.order, pagination.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sortParam = params.get(SORT);
    if (page && sortParam) {
      const sortSplit = sortParam.split(',');
      setPagination({
        ...pagination,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const handlePagination = currentPage =>
    setPagination({
      ...pagination,
      activePage: currentPage,
    });

  const handleSearchRight = e => () => {
    // console.warn('handleSearchRight', e);
    dispatch(getShopSaleDetailList({ shopId, orderId: e.id }));
  };

  const salesForPage = useAppSelector(state => state.sales.salesForPage);
  const salesDetailList = useAppSelector(state => state.sales.salesDetailList);
  const totalItems = useAppSelector(state => state.sales.totalItems);

  useEffect(() => {
    dispatch(resetSalesDetailList()); // 상세 화면 진입 시 salesDetailList 초기화
  }, []);

  useEffect(() => {
    // console.warn('Updated salesDetailList:', salesDetailList);
  }, [salesDetailList]);

  const handleBack = () => {
    navigate('/sales', { replace: true });
    // resetSalesDetailList();
  };

  return (
    <div>
      <h2>
        {date} <Translate contentKey="order.label.details">주문 상세</Translate>
      </h2>
      <Row size="md">
        <Col md="6">
          <Table responsive striped>
            <thead>
              <tr>
                <th className="hand">
                  <Translate contentKey="global.field.id">Price</Translate>
                </th>
                <th className="hand">
                  <Translate contentKey="global.label.price">Price</Translate>
                </th>
                <th className="hand">
                  <Translate contentKey="global.label.price">Price</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {salesForPage.map((user, i) => (
                <tr id={user.login} key={`user-${i}`}>
                  <td>{user.id}</td>
                  <td>{user.shopTableId}</td>
                  <td>{user.totalPrice}</td>
                  <td className="text-end">
                    <Button onClick={handleSearchRight(user)} color="info">
                      <span className="d-none d-md-inline">
                        <Translate contentKey="entity.action.view">보기</Translate>
                      </span>{' '}
                      <FontAwesomeIcon icon={faArrowRightFromBracket} />
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
          {totalItems ? (
            <div className={salesForPage?.length > 0 ? '' : 'd-none'}>
              <div className="justify-content-center d-flex">
                <JhiItemCount page={pagination.activePage} total={totalItems} itemsPerPage={pagination.itemsPerPage} i18nEnabled />
              </div>
              <div className="justify-content-center d-flex">
                <JhiPagination
                  activePage={pagination.activePage}
                  onSelect={handlePagination}
                  maxButtons={5}
                  itemsPerPage={pagination.itemsPerPage}
                  totalItems={totalItems}
                />
              </div>
            </div>
          ) : (
            ''
          )}
          <Button onClick={handleBack} color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
        </Col>
        <Col md="6">
          <div>
            {salesDetailList.map((data, i) => (
              <div key={i}>
                <ul>
                  <li>
                    {data.nameKo} : {data.orderDetailPrice} {data.orderDetailDiscountPrice}
                  </li>
                  <li>
                    <Translate contentKey="global.label.model">모델</Translate> : {data.modelNo}
                  </li>
                  {data.commissionTargetYn && (
                    <li>
                      <Translate contentKey="global.label.commissionPrice">모델</Translate>
                      <ul>
                        <li>
                          <Translate contentKey="global.label.shop">모델</Translate> : {data.shopCommissionPrice}
                        </li>
                        <li>
                          <Translate contentKey="global.label.model">모델</Translate> : {data.modelCommissionPrice}
                        </li>
                        <li>
                          <Translate contentKey="global.label.mama">모델</Translate> : {data.mamaCommissionPrice}
                        </li>
                      </ul>
                    </li>
                  )}
                  {data.commissionTargetYn && data.orderDetailDiscountPrice && (
                    <li>
                      <Translate contentKey="global.label.commissionDiscountPrice">모델</Translate>
                      <ul>
                        <li>
                          <Translate contentKey="global.label.shop">모델</Translate> : {data.discountShopCommissionPrice}
                        </li>
                        <li>
                          <Translate contentKey="global.label.model">모델</Translate> : {data.discountModelCommissionPrice}
                        </li>
                        <li>
                          <Translate contentKey="global.label.mama">모델</Translate> : {data.discountMamaCommissionPrice}
                        </li>
                      </ul>
                    </li>
                  )}
                </ul>
              </div>
            ))}
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default SalesDetail;
