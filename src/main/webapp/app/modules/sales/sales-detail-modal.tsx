import React, { useEffect, useState } from 'react';
import { getPaginationState, JhiItemCount, JhiPagination, Translate } from 'react-jhipster';
import { Alert, Button, Col, Table, Modal, ModalBody, ModalFooter, ModalHeader, Row } from 'reactstrap';
import { Link, useParams, useLocation, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { getShopSaleDetailList, getShopSalesForPage, resetSalesDetailList } from 'app/modules/sales/sales.reducer';

export interface ISalesDetailModalProps {
  shopId: number;
  date: string;
  showDetailModal: boolean;
  handleDetailClose: () => void;
}

const SalesDetailModal = (props: ISalesDetailModalProps) => {
  const dispatch = useAppDispatch();

  const { handleDetailClose, shopId, date } = props;

  const handleOrderDetailClose = () => {
    handleDetailClose();
  };

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [pagination, setPagination] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const getSalesFromProps = () => {
    dispatch(
      getShopSalesForPage({
        id: shopId,
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
    console.warn('shopId, date', shopId, date);
    if (date) {
      getSalesFromProps();
    }
  }, [shopId, date]);

  useEffect(() => {
    if (date) {
      getSalesFromProps();
    }
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

  const salesDetailList = useAppSelector(state => state.sales.salesDetailList);
  const salesForPage = useAppSelector(state => state.sales.salesForPage);
  const totalItems = useAppSelector(state => state.sales.totalItems);

  return (
    <Modal
      isOpen={props.showDetailModal}
      toggle={handleOrderDetailClose}
      backdrop="static"
      id="order-refund-page"
      autoFocus={false}
      className="modal-xl" // 모달 너비 키움
    >
      <ModalHeader id="order-title" data-cy="orderTitle" toggle={handleDetailClose}>
        <Translate contentKey="global.menu.sales">매출</Translate>
      </ModalHeader>
      <ModalBody>
        <Row style={{ minHeight: '500px' }}>
          <Col md="12">
            <div className="mb-3"></div>

            <div className="d-flex gap-4">
              <div style={{ width: '50%' }} className="flex-grow-1 border rounded p-3 shadow-sm">
                <h5 className="mb-3">
                  <Translate contentKey="order.label.details">주문 내역</Translate>
                </h5>

                <Table responsive striped>
                  <thead>
                    <tr>
                      <th className="hand">
                        <Translate contentKey="global.field.no">번호</Translate>
                      </th>
                      <th className="hand">
                        <Translate contentKey="global.label.price">금액</Translate>
                      </th>
                      <th />
                    </tr>
                  </thead>
                  <tbody>
                    {salesForPage.map((user, i) => (
                      <tr id={user.login} key={`user-${i}`}>
                        <td>{user.id}</td>
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
              </div>

              <div style={{ width: '50%' }} className="flex-grow-1 border rounded p-3 shadow-sm">
                <h5 className="mb-3">
                  <Translate contentKey="order.label.items">내역</Translate>
                </h5>

                {salesDetailList.map((data, i) => (
                  <div key={i} className="border rounded p-3 mb-3">
                    <div className="d-flex justify-content-between fw-bold text-muted">
                      <span>
                        <Translate contentKey="global.label.model">모델</Translate>
                      </span>
                      <span>{data.modelNo}</span>
                    </div>
                    <div className="d-flex justify-content-between fw-bold text-muted">
                      <span>{data.nameKo}</span>
                      <span>{data.orderDetailPrice?.toLocaleString()}</span>
                    </div>
                    <div className="d-flex justify-content-between fw-bold text-muted mb-2">
                      <span>
                        <Translate contentKey="global.label.discount">할인</Translate>
                      </span>
                      <span>{data.orderDetailDiscountPrice?.toLocaleString()}</span>
                    </div>

                    {data.commissionTargetYn && (
                      <>
                        {/* 수수료 */}
                        <div className="d-flex justify-content-between fw-bold text-danger">
                          <span>
                            <Translate contentKey="global.label.commissionPrice">커미션 금액</Translate>
                          </span>
                          <span></span>
                        </div>
                        <div className="d-flex justify-content-between">
                          <span>
                            <Translate contentKey="global.label.shop">상점</Translate>
                          </span>
                          <span>{data.shopCommissionPrice?.toLocaleString()}</span>
                        </div>
                        <div className="d-flex justify-content-between">
                          <span>
                            <Translate contentKey="global.label.model">모델</Translate>
                          </span>
                          <span>{data.modelCommissionPrice?.toLocaleString()}</span>
                        </div>
                        <div className="d-flex justify-content-between mb-2">
                          <span>
                            <Translate contentKey="global.label.mama">마마</Translate>
                          </span>
                          <span>{data.mamaCommissionPrice?.toLocaleString()}</span>
                        </div>

                        {/* 할인 수수료 */}
                        {data.orderDetailDiscountPrice && (
                          <>
                            <div className="d-flex justify-content-between fw-bold text-danger">
                              <span>
                                <Translate contentKey="global.label.commissionDiscountPrice">커미션 할인 금액</Translate>
                              </span>
                              <span></span>
                            </div>
                            <div className="d-flex justify-content-between">
                              <span>
                                <Translate contentKey="global.label.shop">상점</Translate>
                              </span>
                              <span>{data.discountShopCommissionPrice?.toLocaleString()}</span>
                            </div>
                            <div className="d-flex justify-content-between">
                              <span>
                                <Translate contentKey="global.label.model">모델</Translate>
                              </span>
                              <span>{data.discountModelCommissionPrice?.toLocaleString()}</span>
                            </div>
                            <div className="d-flex justify-content-between">
                              <span>
                                <Translate contentKey="global.label.mama">마마</Translate>
                              </span>
                              <span>{data.discountMamaCommissionPrice?.toLocaleString()}</span>
                            </div>
                          </>
                        )}
                      </>
                    )}
                  </div>
                ))}
              </div>
            </div>
          </Col>
        </Row>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleOrderDetailClose} tabIndex={1}>
          <Translate contentKey="entity.action.close">닫기</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default SalesDetailModal;
