import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { getPaginationState, JhiItemCount, JhiPagination, TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';

import { APP_LOCAL_TIMESTAMP_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getShopsAsAdminForPage, updateShop } from './shop-management.reducer';

export const ShopManagement = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [pagination, setPagination] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const getUsersFromProps = () => {
    dispatch(
      getShopsAsAdminForPage({
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: `${pagination.sort},${pagination.order}`,
      }),
    );
    const endURL = `?page=${pagination.activePage}&sort=${pagination.sort},${pagination.order}`;
    console.warn('pageLocation', pageLocation);
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    getUsersFromProps();
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

  const sort = p => () =>
    setPagination({
      ...pagination,
      order: pagination.order === ASC ? DESC : ASC,
      sort: p,
    });

  const handlePagination = currentPage =>
    setPagination({
      ...pagination,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    getUsersFromProps();
  };

  const toggleActive = shop => () => {
    dispatch(
      updateShop({
        ...shop,
        activated: !shop.activated,
      }),
    );
  };

  const shopsForPage = useAppSelector(state => state.shopManagement.shopsForPage);
  const totalItems = useAppSelector(state => state.shopManagement.totalItems);
  const loading = useAppSelector(state => state.shopManagement.loading);
  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = pagination.sort;
    const order = pagination.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="shop-management-page-heading" data-cy="shopManagementPageHeading">
        <Translate contentKey="shopManagement.home.title">Shops</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="shopManagement.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="new" className="btn btn-primary jh-create-entity">
            <FontAwesomeIcon icon="plus" /> <Translate contentKey="shopManagement.home.createLabel">Create a new shop</Translate>
          </Link>
        </div>
      </h2>
      <Table responsive striped>
        <thead>
          <tr>
            <th className="hand" onClick={sort('id')}>
              <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
            </th>
            <th className="hand" onClick={sort('nameKo')}>
              <Translate contentKey="shopManagement.nameKo">nameKo</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('nameKo')} />
            </th>
            <th className="hand" onClick={sort('nameEn')}>
              <Translate contentKey="shopManagement.nameEn">nameEn</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('nameEn')} />
            </th>
            <th className="hand" onClick={sort('type')}>
              <Translate contentKey="shopManagement.type">type</Translate>
              <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
            </th>
            <th className="hand" onClick={sort('workStartTime')}>
              <Translate contentKey="shopManagement.workStartTime">근무시작시간</Translate>
              <FontAwesomeIcon icon={getSortIconByFieldName('workStartTime')} />
            </th>
            <th className="hand" onClick={sort('activated')}>
              <Translate contentKey="shopManagement.activated">Activated</Translate>{' '}
              <FontAwesomeIcon icon={getSortIconByFieldName('activated')} />
            </th>
            <th className="hand" onClick={sort('createdBy')}>
              <Translate contentKey="shopManagement.createdBy">Created By</Translate>{' '}
              <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
            </th>
            <th className="hand" onClick={sort('createdDate')}>
              <Translate contentKey="shopManagement.createdDate">Created Date</Translate>{' '}
              <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
            </th>
            <th className="hand" onClick={sort('lastModifiedBy')}>
              <Translate contentKey="shopManagement.lastModifiedBy">Last Modified By</Translate>{' '}
              <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
            </th>
            <th id="modified-date-sort" className="hand" onClick={sort('lastModifiedDate')}>
              <Translate contentKey="shopManagement.lastModifiedDate">Last Modified Date</Translate>{' '}
              <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
            </th>
            <th />
          </tr>
        </thead>
        <tbody>
          {shopsForPage.map((shop, i) => (
            <tr id={shop.id} key={`shop-${i}`}>
              <td>
                <Button tag={Link} to={`${shop.id}`} color="link" size="sm">
                  {shop.id}
                </Button>
              </td>
              <td>{shop.nameKo}</td>
              <td>{shop.nameEn}</td>
              <td>{shop.type}</td>
              <td>{shop.workStartTime}</td>
              <td>
                {shop.activated ? (
                  <Button color="success" onClick={toggleActive(shop)}>
                    <Translate contentKey="shopManagement.activated">Activated</Translate>
                  </Button>
                ) : (
                  <Button color="danger" onClick={toggleActive(shop)}>
                    <Translate contentKey="shopManagement.deactivated">Deactivated</Translate>
                  </Button>
                )}
              </td>
              <td>{shop.createdBy}</td>
              <td>
                {shop.createdDate ? (
                  <TextFormat value={shop.createdDate} type="date" format={APP_LOCAL_TIMESTAMP_FORMAT} blankOnInvalid />
                ) : null}
              </td>
              <td>{shop.lastModifiedBy}</td>
              <td>
                {shop.lastModifiedDate ? (
                  <TextFormat value={shop.lastModifiedDate} type="date" format={APP_LOCAL_TIMESTAMP_FORMAT} blankOnInvalid />
                ) : null}
              </td>
              <td className="text-end">
                <div className="btn-group flex-btn-group-container">
                  <Button tag={Link} to={`${shop.id}`} color="info" size="sm">
                    <FontAwesomeIcon icon="eye" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.view">View</Translate>
                    </span>
                  </Button>
                  <Button tag={Link} to={`${shop.id}/edit`} color="primary" size="sm">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.edit">Edit</Translate>
                    </span>
                  </Button>
                  <Button tag={Link} to={`${shop.id}/user-mapping`} color="danger" size="sm">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="shopManagement.button.userMapping">사용자 연결</Translate>
                    </span>
                  </Button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      {totalItems ? (
        <div className={shopsForPage?.length > 0 ? '' : 'd-none'}>
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
  );
};

export default ShopManagement;
