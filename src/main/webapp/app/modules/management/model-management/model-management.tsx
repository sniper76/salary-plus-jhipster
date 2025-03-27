import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Badge, Button, Table } from 'reactstrap';
import { getPaginationState, JhiItemCount, JhiPagination, TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUsersAsAdmin, updateUser } from './model-management.reducer';
import { getUserShops } from 'app/modules/order/order.reducer';

export const ModelManagement = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();
  const shops = useAppSelector(state => state.orders.shops);

  const [pagination, setPagination] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [selectedValue, setSelectedValue] = useState(-1);

  const getUsersFromProps = () => {
    dispatch(
      getUsersAsAdmin({
        id: selectedValue,
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

  const handleSelect = e => {
    setSelectedValue(e.target.value);
  };

  useEffect(() => {
    dispatch(getUserShops());
  }, []);

  useEffect(() => {
    if (shops.length > 0) {
      setSelectedValue(shops[0].id);
    }
  }, [shops]);

  useEffect(() => {
    getUsersFromProps();
  }, [pagination.activePage, pagination.order, pagination.sort, selectedValue]);

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

  const users = useAppSelector(state => state.modelManagement.users);
  const totalItems = useAppSelector(state => state.modelManagement.totalItems);
  const loading = useAppSelector(state => state.modelManagement.loading);
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
      <h2 id="user-management-page-heading" data-cy="userManagementPageHeading">
        <Translate contentKey="userManagement.home.title">모델</Translate>
        <div className="d-flex justify-content-end">
          <select onChange={handleSelect} value={selectedValue} className="custom-number-input">
            {shops.map(shop => (
              <option value={shop.id} key={shop.id}>
                {shop.nameKo}
              </option>
            ))}
          </select>
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="userManagement.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${selectedValue}/new`} className="btn btn-primary jh-create-entity">
            <FontAwesomeIcon icon="plus" /> <Translate contentKey="userManagement.home.createLabel">Create a new user</Translate>
          </Link>
        </div>
      </h2>
      <Table responsive striped>
        <thead>
          <tr>
            <th className="hand" onClick={sort('id')}>
              <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
            </th>
            <th className="hand" onClick={sort('login')}>
              <Translate contentKey="userManagement.login">Login</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('login')} />
            </th>
            <th className="hand" onClick={sort('email')}>
              <Translate contentKey="userManagement.email">Email</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
            </th>
            <th className="hand" onClick={sort('langKey')}>
              <Translate contentKey="userManagement.langKey">Lang Key</Translate>{' '}
              <FontAwesomeIcon icon={getSortIconByFieldName('langKey')} />
            </th>
            <th className="hand" onClick={sort('langKey')}>
              <Translate contentKey="userManagement.langKey">Lang Key</Translate>{' '}
              <FontAwesomeIcon icon={getSortIconByFieldName('langKey')} />
            </th>
            <th className="hand" onClick={sort('createdDate')}>
              <Translate contentKey="userManagement.createdDate">Created Date</Translate>{' '}
              <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
            </th>
            <th className="hand" onClick={sort('lastModifiedBy')}>
              <Translate contentKey="userManagement.lastModifiedBy">Last Modified By</Translate>{' '}
              <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
            </th>
            <th id="modified-date-sort" className="hand" onClick={sort('lastModifiedDate')}>
              <Translate contentKey="userManagement.lastModifiedDate">Last Modified Date</Translate>{' '}
              <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
            </th>
            <th />
          </tr>
        </thead>
        <tbody>
          {users.length <= 0 && (
            <tr>
              <td colSpan={9} className="text-align-center">
                <Translate contentKey="global.messages.info.noDataList">조회된 데이터가 없습니다.</Translate>
              </td>
            </tr>
          )}
          {users.map((user, i) => (
            <tr id={user.login} key={`user-${i}`}>
              <td>
                <Button tag={Link} to={`${user.id}/${selectedValue}`} color="link" size="sm">
                  {user.id}
                </Button>
              </td>
              <td>{user.login}</td>
              <td>{user.email}</td>
              <td>{user.langKey}</td>
              <td>
                {user.authorities
                  ? user.authorities.map((authority, j) => (
                      <div key={`user-auth-${i}-${j}`}>
                        <Badge color="info">{authority}</Badge>
                      </div>
                    ))
                  : null}
              </td>
              <td>
                {user.createdDate ? <TextFormat value={user.createdDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid /> : null}
              </td>
              <td>{user.lastModifiedBy}</td>
              <td>
                {user.lastModifiedDate ? (
                  <TextFormat value={user.lastModifiedDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
                ) : null}
              </td>
              <td className="text-end">
                <div className="btn-group flex-btn-group-container">
                  <Button tag={Link} to={`${user.id}/${selectedValue}`} color="info" size="sm">
                    <FontAwesomeIcon icon="eye" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.view">View</Translate>
                    </span>
                  </Button>
                  <Button tag={Link} to={`${user.id}/${selectedValue}/edit`} color="primary" size="sm">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.edit">Edit</Translate>
                    </span>
                  </Button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      {totalItems ? (
        <div className={users?.length > 0 ? '' : 'd-none'}>
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

export default ModelManagement;
