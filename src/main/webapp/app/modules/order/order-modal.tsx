import React, { useEffect, useState } from 'react';
import { Translate, ValidatedField } from 'react-jhipster';
import { Alert, Button, Col, Form, Modal, ModalBody, ModalFooter, ModalHeader, Row } from 'reactstrap';
import { useForm } from 'react-hook-form';
import './order.scss';
import { IShopSalesItem } from 'app/shared/model/shopSalesItem.model';
import { IModelUser } from 'app/shared/model/modelUser.model';
import { IShopTable } from 'app/shared/model/shopTable.model';
import { useAppDispatch } from 'app/config/store';
import { deleteOrderDetail, getShopOrders } from 'app/modules/order/order.reducer';
import { IOrderDetail } from 'app/shared/model/orderDetail.model';

export interface IOrderModalProps {
  showModal: boolean;
  orderError: boolean;
  handleOrder: (obj: any) => void;
  handleClose: () => void;
  salesItems: ReadonlyArray<IShopSalesItem>;
  models: ReadonlyArray<IModelUser>;
  tables: ReadonlyArray<IShopTable>;
  orderDetails: IOrderDetail[];
  orderId: null;
  tableId: null;
  shopId: number;
  date: string;
}

const OrderModal = (props: IOrderModalProps) => {
  const orderHandleProps = obj => {
    props.handleOrder(obj);
  };

  const {
    handleSubmit,
    register,
    reset,
    watch,
    getValues,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  const dispatch = useAppDispatch();

  const { orderError, handleClose, shopId, date } = props;

  const [leftItems, setLeftItems] = useState([]); // 왼쪽 div 아이템들
  const [draggedItem, setDraggedItem] = useState(null); // 드래그 중인 아이템

  // const handleOrderSubmit = e => {
  //   console.warn('handleOrderSubmit', e, leftItems, errors);
  //   if (Object.keys(errors).length > 0) {
  //     return; // 에러가 있으면 제출을 막음
  //   }
  //   handleSubmit(orderHandleProps)(e);
  //   handleClose();
  // };

  const handleOrderSubmit = handleSubmit(data => {
    // handleSubmit 을 먼저 실행함
    // console.warn('handleOrderSubmit', data, leftItems, errors);
    // if (Object.keys(errors).length > 0) {
    //   return; // 에러가 있으면 제출을 막음
    // }
    orderHandleProps(data);
    handleClose();
  });

  const handleDragStart = item => {
    // console.warn('handleDragStart', item);
    setDraggedItem(item); // 드래그 시작 시 아이템 저장
  };

  const handleDragOver = e => {
    e.preventDefault(); // 기본 동작 방지 (drop 이벤트 허용)
  };

  const handleDrop = () => {
    if (draggedItem) {
      // reset();
      handleResetExceptTableId();
      setLeftItems(prev => [...prev, draggedItem]); // 왼쪽에 복사 추가
    }
  };

  const handleResetExceptTableId = () => {
    const currentValues = getValues();
    reset(
      {
        tableId: currentValues.tableId, // 유지하고 싶은 필드
      },
      {
        keepErrors: false,
        keepDirty: false,
        keepTouched: false,
      },
    );
  };

  const handleRemove = (index, item) => {
    setLeftItems(prev => prev.filter((_, i) => i !== index)); // 왼쪽에서 삭제
    // console.warn('handleRemove', item);
    if (item.orderDetailId) {
      dispatch(deleteOrderDetail({ orderId: props.orderId, orderDetailId: item.orderDetailId })).then(() => {
        dispatch(getShopOrders({ shopId, date }));
      });
    }
  };

  useEffect(() => {
    reset(); // react-hook-form 의 상태도 초기화
    if (props.orderId && props.orderDetails && props.orderDetails.length > 0) {
      // console.warn('OrderModal: 기존 주문 불러오기', props.orderDetails);
      setLeftItems([...props.orderDetails]); // 기존 주문 정보로 leftItems 설정
    } else if (!props.orderId) {
      // console.warn('OrderModal: 새로운 주문 초기화');
      setLeftItems([]); // 새로운 주문일 경우 leftItems 초기화
    }
  }, [props.orderDetails, props.orderId, reset]);

  // leftItems 값이 실제로 업데이트된 이후 확인
  useEffect(() => {
    // console.warn('OrderModal leftItems Updated', leftItems);
  }, [props]);

  // watch() 는 폼의 모든 값을 실시간으로 관찰합니다.
  const allValues = watch();

  useEffect(() => {
    // console.warn('Current Form Values:', allValues); // 값이 변경될 때마다 출력
  }, [allValues]);

  return (
    <Modal
      isOpen={props.showModal}
      toggle={handleClose}
      backdrop="static"
      id="order-page"
      autoFocus={false}
      className="modal-lg" // 모달 너비 키움
    >
      <Form onSubmit={handleOrderSubmit}>
        <ModalHeader id="order-title" data-cy="orderTitle" toggle={handleClose}>
          <Translate contentKey="global.menu.order">주문</Translate>
        </ModalHeader>
        <ModalBody>
          <Row>
            <Col md="12">
              <div className="mb-3">
                {props.orderId && (
                  <ValidatedField type="hidden" register={register} id="orderId" name="orderId" data-cy="orderId" value={props.orderId} />
                )}
                <ValidatedField
                  className="form-select"
                  type="select"
                  register={register}
                  id="tableId"
                  name="tableId"
                  data-cy="tableId"
                  defaultValue={props.tableId}
                >
                  {props.tables.map((data, idx) => (
                    <option value={data.id} key={idx}>
                      {data.no}
                    </option>
                  ))}
                </ValidatedField>
              </div>

              <div className="d-flex gap-4">
                {/* 왼쪽: 선택된 아이템들 */}
                <div className="flex-grow-1 border rounded p-3 shadow-sm" onDragOver={handleDragOver} onDrop={handleDrop}>
                  <h5 className="mb-3">
                    <Translate contentKey="order.label.selectedDiscounts">선택된 할인</Translate>
                  </h5>
                  {leftItems.map((item, index) => (
                    <div key={index} className="mb-3 border rounded p-2 shadow-sm">
                      <div className="d-flex justify-content-between align-items-center mb-2">
                        <strong>{item.nameKo}</strong>
                        <Button size="sm" color="danger" onClick={() => handleRemove(index, item)}>
                          ✖
                        </Button>
                      </div>
                      {item.commissionTargetYn && (
                        <ValidatedField
                          className="form-select mb-2"
                          register={register}
                          type="select"
                          id={`modelIds[${index}]`}
                          name={`modelIds[${index}]`}
                          data-cy={`modelIds[${index}]`}
                          defaultValue={item.modelId}
                        >
                          {props.models.map((model, idx) => (
                            <option value={model.id} key={idx}>
                              {model.modelNo}
                            </option>
                          ))}
                        </ValidatedField>
                      )}
                      {errors?.prices && (
                        <div>
                          <Alert color="warning" fade={false}>
                            <Translate contentKey="error.order.empty.price">Price cannot be empty.</Translate>
                          </Alert>
                        </div>
                      )}
                      {item.snackYn ? (
                        <ValidatedField
                          register={register}
                          type="number"
                          id={`prices[${index}]`}
                          name={`prices[${index}]`}
                          data-cy={`prices[${index}]`}
                          validate={{
                            required: { value: true, message: 'Price is required.' },
                            min: { value: 1, message: 'Price must be a positive number.' },
                          }}
                          defaultValue={item.price || 0}
                        />
                      ) : (
                        <ValidatedField
                          register={register}
                          type="hidden"
                          id={`prices[${index}]`}
                          name={`prices[${index}]`}
                          data-cy={`prices[${index}]`}
                          value={item.price || 0}
                        />
                      )}
                      <ValidatedField
                        type="hidden"
                        register={register}
                        id={`salesItemIds[${index}]`}
                        name={`salesItemIds[${index}]`}
                        data-cy={`salesItemIds[${index}]`}
                        value={item.id}
                      />
                      <ValidatedField
                        type="hidden"
                        register={register}
                        id={`orderDetailIds[${index}]`}
                        name={`orderDetailIds[${index}]`}
                        data-cy={`orderDetailIds[${index}]`}
                        value={item.orderDetailId}
                      />
                    </div>
                  ))}
                </div>

                <div className="flex-grow-1 border rounded p-3 shadow-sm">
                  <h5 className="mb-3">
                    <Translate contentKey="order.label.discountApplicable">적용 가능한 할인 항목</Translate>
                  </h5>
                  {props.salesItems.map((item, index) => (
                    <div
                      key={index}
                      className="border rounded p-2 mb-2 shadow-sm text-center cursor-pointer"
                      draggable
                      onDragStart={() => handleDragStart(item)}
                      style={{ cursor: 'grab' }}
                    >
                      {item.nameKo}
                    </div>
                  ))}
                </div>
              </div>
            </Col>
          </Row>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={handleClose} tabIndex={1}>
            <Translate contentKey="entity.action.cancel">취소</Translate>
          </Button>{' '}
          <Button color="primary" type="submit" data-cy="submit">
            <Translate contentKey="order.form.button">주문하기</Translate>
          </Button>
        </ModalFooter>
      </Form>
    </Modal>
  );
};

export default OrderModal;
