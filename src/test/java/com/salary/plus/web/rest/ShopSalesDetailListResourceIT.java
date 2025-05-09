package com.salary.plus.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shiver.me.timbers.data.random.RandomLongs.somePositiveLong;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salary.plus.IntegrationTest;
import com.salary.plus.domain.Shop;
import com.salary.plus.domain.ShopBaseSalary;
import com.salary.plus.domain.ShopOrder;
import com.salary.plus.domain.ShopOrderDetail;
import com.salary.plus.domain.ShopOrderDetailDiscount;
import com.salary.plus.domain.ShopPenalty;
import com.salary.plus.domain.ShopSalesItem;
import com.salary.plus.domain.ShopSalesItemDiscount;
import com.salary.plus.domain.ShopSalesRefund;
import com.salary.plus.domain.ShopUserBaseSalaryMapping;
import com.salary.plus.domain.ShopUserDailySalary;
import com.salary.plus.domain.ShopUserMapping;
import com.salary.plus.domain.ShopUserPenaltyMapping;
import com.salary.plus.domain.ShopUserSalesSalary;
import com.salary.plus.domain.User;
import com.salary.plus.enums.DiscountType;
import com.salary.plus.enums.PenaltyType;
import com.salary.plus.enums.ShopType;
import com.salary.plus.repository.ShopBaseSalaryRepository;
import com.salary.plus.repository.ShopOrderDetailDiscountRepository;
import com.salary.plus.repository.ShopOrderDetailRepository;
import com.salary.plus.repository.ShopOrderRepository;
import com.salary.plus.repository.ShopPenaltyRepository;
import com.salary.plus.repository.ShopRepository;
import com.salary.plus.repository.ShopSalesItemDiscountRepository;
import com.salary.plus.repository.ShopSalesItemRepository;
import com.salary.plus.repository.ShopSalesRefundRepository;
import com.salary.plus.repository.ShopUserBaseSalaryMappingRepository;
import com.salary.plus.repository.ShopUserDailySalaryRepository;
import com.salary.plus.repository.ShopUserMappingRepository;
import com.salary.plus.repository.ShopUserPenaltyMappingRepository;
import com.salary.plus.repository.ShopUserSalesSalaryRepository;
import com.salary.plus.repository.UserRepository;
import com.salary.plus.security.AuthoritiesConstants;
import com.salary.plus.service.provider.DateProvider;
import com.salary.plus.utils.DateTimeFormatUtil;
import com.salary.plus.utils.DateUtils;
import com.salary.plus.utils.ObjectMapperUtil;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class ShopSalesDetailListResourceIT {

    private static final String TARGET_URL = "/api/shops/{shopId}/sales/dates/{date}";

    @Autowired
    protected ObjectMapperUtil objectMapperUtil;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopUserDailySalaryRepository shopUserDailySalaryRepository;

    @Autowired
    private ShopUserBaseSalaryMappingRepository shopUserBaseSalaryMappingRepository;

    @Autowired
    private ShopBaseSalaryRepository shopBaseSalaryRepository;

    @Autowired
    private ShopUserPenaltyMappingRepository shopUserPenaltyMappingRepository;

    @Autowired
    private ShopPenaltyRepository shopPenaltyRepository;

    @Autowired
    private ShopUserMappingRepository shopUserMappingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopOrderRepository shopOrderRepository;

    @Autowired
    private ShopOrderDetailRepository shopOrderDetailRepository;

    @Autowired
    private ShopUserSalesSalaryRepository shopUserSalesSalaryRepository;

    @Autowired
    private ShopSalesItemRepository shopSalesItemRepository;

    @Autowired
    private ShopSalesRefundRepository shopSalesRefundRepository;

    @Autowired
    private ShopOrderDetailDiscountRepository shopOrderDetailDiscountRepository;

    @Autowired
    private ShopSalesItemDiscountRepository shopSalesItemDiscountRepository;

    @Mock
    private DateProvider dateProvider;

    @Autowired
    private MockMvc restUserMockMvc;

    private Map<String, Object> params;

    private MvcResult callApi(ResultMatcher resultMatcher, Long shopId, String date) throws Exception {
        return restUserMockMvc
            .perform(
                get(TARGET_URL, shopId, date)
                    .params(toMultiValueMap(params))
                    .contentType(APPLICATION_JSON_VALUE)
                    .accept(APPLICATION_JSON_VALUE)
            )
            .andExpect(resultMatcher)
            .andReturn();
    }

    public MultiValueMap<String, String> toMultiValueMap(Map<String, Object> map) {
        final MultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();

        map.forEach((key, value) -> {
            if (value instanceof List) {
                linkedMultiValueMap.put(key, (List) value);
            } else {
                linkedMultiValueMap.add(key, value.toString());
            }
        });

        return linkedMultiValueMap;
    }

    public <T> T getResult(MvcResult response, Class<T> responseType) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapperUtil.toResponse(response.getResponse().getContentAsString(), responseType);
    }

    @Nested
    class WhenSuccess {

        private Long shopId;
        private String date;

        private Integer baseSalaryPrice = 500;
        private Long userId;
        private Long shopPenaltyId;
        private Long orderId;

        private final Integer penaltyPrice = 500;
        private final Integer longTimeBarFinePrice = 5000;
        private final Integer ladyDrinkPrice = 350;
        private final Integer guestDrinkPrice = 150;
        private final Integer longTimeBarFinePriceYesterday = 4000;
        private final Integer ladyDrinkPriceYesterday = 350;
        private final Integer guestDrinkPriceYesterday = 150;
        private final Integer discountPrice = 0;
        private final Integer refundPrice = 0;

        private void mockData() {
            Shop shop = new Shop();
            shop.setType(ShopType.BAR);
            shop.setNameKo("shop1");
            shop.setNameEn("shop1");
            shop.setWorkStartTime("09:00:00");
            shopRepository.saveAndFlush(shop);

            final User user = userRepository.findOneByLogin("user").orElseThrow();

            shopId = shop.getId();
            userId = user.getId();
            date = DateUtils.getFormatted(DateTimeFormatUtil.yyyy_MM_dd());

            ShopUserMapping shopUserMapping = new ShopUserMapping();
            shopUserMapping.setShopId(shop.getId());
            shopUserMapping.setUserId(user.getId());
            shopUserMappingRepository.saveAndFlush(shopUserMapping);

            ShopBaseSalary shopBaseSalary = new ShopBaseSalary();
            shopBaseSalary.setShopId(shopId);
            shopBaseSalary.setPrice(baseSalaryPrice);
            shopBaseSalary.setNameKo("test name ko");
            shopBaseSalary.setNameEn("test name en");
            shopBaseSalaryRepository.saveAndFlush(shopBaseSalary);

            ShopUserBaseSalaryMapping shopUserBaseSalaryMapping = new ShopUserBaseSalaryMapping();
            shopUserBaseSalaryMapping.setUserId(userId);
            shopUserBaseSalaryMapping.setShopBaseSalaryId(shopBaseSalary.getId());
            shopUserBaseSalaryMappingRepository.saveAndFlush(shopUserBaseSalaryMapping);

            ShopUserDailySalary shopUserDailySalary = new ShopUserDailySalary();
            shopUserDailySalary.setShopId(shopId);
            shopUserDailySalary.setUserId(userId);
            shopUserDailySalary.setDate(date);
            shopUserDailySalary.setPrice(shopBaseSalary.getPrice());
            shopUserDailySalaryRepository.saveAndFlush(shopUserDailySalary);
        }

        private void mockRefundData(Long orderId, String orderDate, Integer shopPrice, Integer modelPrice, Integer mamaPrice) {
            ShopSalesRefund shopSalesRefund = new ShopSalesRefund();
            shopSalesRefund.setShopId(shopId);
            shopSalesRefund.setOrderId(orderId);
            shopSalesRefund.setDate(orderDate);
            shopSalesRefund.setShopPrice(shopPrice);
            shopSalesRefund.setModelPrice(modelPrice);
            shopSalesRefund.setMamaPrice(mamaPrice);
            shopSalesRefundRepository.save(shopSalesRefund);
        }

        private void mockDiscountData(Long salesItemId, Long orderDetailId, Integer shopPrice, Integer modelPrice, Integer mamaPrice) {
            ShopSalesItemDiscount shopSalesItemDiscount = new ShopSalesItemDiscount();
            shopSalesItemDiscount.setShopSalesItemId(salesItemId);
            shopSalesItemDiscount.setNameKo("test discount ko");
            shopSalesItemDiscount.setNameEn("test discount en");
            shopSalesItemDiscount.setType(DiscountType.PRICE);
            shopSalesItemDiscount.setPrice(shopPrice + modelPrice + mamaPrice);
            shopSalesItemDiscount.setShopCommissionPrice(shopPrice);
            shopSalesItemDiscount.setModelCommissionPrice(modelPrice);
            shopSalesItemDiscount.setMamaCommissionPrice(mamaPrice);
            shopSalesItemDiscountRepository.saveAndFlush(shopSalesItemDiscount);

            ShopOrderDetailDiscount shopOrderDetailDiscount = new ShopOrderDetailDiscount();
            shopOrderDetailDiscount.setShopOrderDetailId(orderDetailId);
            shopOrderDetailDiscount.setShopSalesItemDiscountId(shopSalesItemDiscount.getId());
            shopOrderDetailDiscount.setPrice(shopPrice + modelPrice + mamaPrice);
            shopOrderDetailDiscountRepository.save(shopOrderDetailDiscount);
        }

        private ShopOrderDetail mockOrderData(
            String orderDate,
            Integer longTimeBarFinePrice,
            Integer ladyDrinkPrice,
            Integer guestDrinkPrice,
            int discountPrice,
            int refundPrice
        ) {
            final Integer totalPrice = longTimeBarFinePrice + ladyDrinkPrice + guestDrinkPrice;

            ShopOrder shopOrder = new ShopOrder();
            shopOrder.setShopId(shopId);
            shopOrder.setShopTableId(somePositiveLong());
            shopOrder.setPaid(false);
            shopOrder.setDate(orderDate);
            shopOrder.setTotalPrice(totalPrice);
            shopOrder.setDiscountPrice(discountPrice);
            shopOrder.setRefundPrice(refundPrice);
            shopOrderRepository.saveAndFlush(shopOrder);

            orderId = shopOrder.getId();

            ShopSalesItem shopSalesItem1 = new ShopSalesItem();
            shopSalesItem1.setShopId(shopId);
            shopSalesItem1.setNameKo("item1 ko");
            shopSalesItem1.setNameEn("item1 en");
            shopSalesItem1.setPrice(longTimeBarFinePrice);
            shopSalesItem1.setCommissionTargetYn(true);
            shopSalesItem1.setShopCommissionPrice(0);
            shopSalesItem1.setMamaCommissionPrice(0);
            shopSalesItem1.setModelCommissionPrice(0);
            shopSalesItem1.setSnackYn(false);
            shopSalesItemRepository.saveAndFlush(shopSalesItem1);

            ShopSalesItem shopSalesItem2 = new ShopSalesItem();
            shopSalesItem2.setShopId(shopId);
            shopSalesItem2.setNameKo("item2 ko");
            shopSalesItem2.setNameEn("item2 en");
            shopSalesItem2.setPrice(ladyDrinkPrice);
            shopSalesItem2.setCommissionTargetYn(true);
            shopSalesItem2.setShopCommissionPrice(0);
            shopSalesItem2.setMamaCommissionPrice(0);
            shopSalesItem2.setModelCommissionPrice(0);
            shopSalesItem2.setSnackYn(false);
            shopSalesItemRepository.saveAndFlush(shopSalesItem2);

            ShopSalesItem shopSalesItem3 = new ShopSalesItem();
            shopSalesItem3.setShopId(shopId);
            shopSalesItem3.setNameKo("item3 ko");
            shopSalesItem3.setNameEn("item3 en");
            shopSalesItem3.setPrice(guestDrinkPrice);
            shopSalesItem3.setCommissionTargetYn(false);
            shopSalesItem3.setShopCommissionPrice(0);
            shopSalesItem3.setMamaCommissionPrice(0);
            shopSalesItem3.setModelCommissionPrice(0);
            shopSalesItem3.setSnackYn(false);
            shopSalesItemRepository.saveAndFlush(shopSalesItem3);

            ShopOrderDetail shopOrderDetail1 = new ShopOrderDetail();
            shopOrderDetail1.setShopOrderId(shopOrder.getId());
            shopOrderDetail1.setShopSalesItemId(shopSalesItem1.getId());
            shopOrderDetail1.setPrice(longTimeBarFinePrice);
            shopOrderDetailRepository.saveAndFlush(shopOrderDetail1);

            ShopOrderDetail shopOrderDetail2 = new ShopOrderDetail();
            shopOrderDetail2.setShopOrderId(shopOrder.getId());
            shopOrderDetail2.setShopSalesItemId(shopSalesItem2.getId());
            shopOrderDetail2.setPrice(ladyDrinkPrice);
            shopOrderDetailRepository.saveAndFlush(shopOrderDetail2);

            ShopOrderDetail shopOrderDetail3 = new ShopOrderDetail();
            shopOrderDetail3.setShopOrderId(shopOrder.getId());
            shopOrderDetail3.setShopSalesItemId(shopSalesItem3.getId());
            shopOrderDetail3.setPrice(guestDrinkPrice);
            shopOrderDetailRepository.saveAndFlush(shopOrderDetail3);

            ShopUserSalesSalary shopUserSalesSalary1 = new ShopUserSalesSalary();
            shopUserSalesSalary1.setShopOrderDetailId(shopOrderDetail1.getId());
            shopUserSalesSalary1.setUserId(userId);
            shopUserSalesSalary1.setPrice(longTimeBarFinePrice);
            shopUserSalesSalaryRepository.saveAndFlush(shopUserSalesSalary1);

            ShopUserSalesSalary shopUserSalesSalary2 = new ShopUserSalesSalary();
            shopUserSalesSalary2.setShopOrderDetailId(shopOrderDetail2.getId());
            shopUserSalesSalary2.setUserId(userId);
            shopUserSalesSalary2.setPrice(ladyDrinkPrice);
            shopUserSalesSalaryRepository.saveAndFlush(shopUserSalesSalary2);

            return shopOrderDetail1;
        }

        @DisplayName("할인 환불 둘다 있는 경우 첫번째는 환불 두번째는 할인")
        @Nested
        class WhenAlreadyCheckInAndHasLatePenalty {

            @BeforeEach
            void setUp() {
                mockData();
                final ShopOrderDetail shopOrderDetail0 = mockOrderData(
                    date,
                    longTimeBarFinePrice,
                    ladyDrinkPrice,
                    guestDrinkPrice,
                    discountPrice,
                    1500
                );
                mockRefundData(shopOrderDetail0.getShopOrderId(), date, 1000, 300, 200);
                final ShopOrderDetail shopOrderDetail1 = mockOrderData(
                    date,
                    longTimeBarFinePriceYesterday,
                    ladyDrinkPriceYesterday,
                    guestDrinkPriceYesterday,
                    1000,
                    refundPrice
                );
                mockDiscountData(shopOrderDetail1.getShopSalesItemId(), shopOrderDetail1.getId(), 500, 300, 200);
                params = Map.of("page", 0, "size", 10, "sort", "id,asc");
            }

            @DisplayName("정상 조회된다.")
            @Test
            @Transactional
            void create() throws Exception {
                final MvcResult mvcResult = callApi(status().isOk(), shopId, date);

                final List response = getResult(mvcResult, List.class);

                final LinkedHashMap data0 = (LinkedHashMap) response.get(0);
                final LinkedHashMap data1 = (LinkedHashMap) response.get(1);
                assertThat(data0.get("totalPrice")).isEqualTo(5500);
                assertThat(data0.get("discountPrice")).isEqualTo(0);
                assertThat(data0.get("refundPrice")).isEqualTo(1500);
                assertThat(data1.get("totalPrice")).isEqualTo(4500);
                assertThat(data1.get("discountPrice")).isEqualTo(1000);
                assertThat(data1.get("refundPrice")).isEqualTo(0);
            }
        }

        @DisplayName("할인 환불 아무것도 없는 경우")
        @Nested
        class WhenNormal {

            @BeforeEach
            void setUp() {
                mockData();
                mockOrderData(date, longTimeBarFinePrice, ladyDrinkPrice, guestDrinkPrice, discountPrice, refundPrice);
                mockOrderData(
                    date,
                    longTimeBarFinePriceYesterday,
                    ladyDrinkPriceYesterday,
                    guestDrinkPriceYesterday,
                    discountPrice,
                    refundPrice
                );

                params = Map.of("page", 0, "size", 10, "sort", "id,asc");
            }

            @DisplayName("정상 조회된다.")
            @Test
            @Transactional
            void create() throws Exception {
                final MvcResult mvcResult = callApi(status().isOk(), shopId, date);

                final List response = getResult(mvcResult, List.class);

                final LinkedHashMap data0 = (LinkedHashMap) response.get(0);
                final LinkedHashMap data1 = (LinkedHashMap) response.get(1);
                assertThat(data0.get("totalPrice")).isEqualTo(5500);
                assertThat(data0.get("discountPrice")).isEqualTo(0);
                assertThat(data0.get("refundPrice")).isEqualTo(0);
                assertThat(data1.get("totalPrice")).isEqualTo(4500);
                assertThat(data1.get("discountPrice")).isEqualTo(0);
                assertThat(data1.get("refundPrice")).isEqualTo(0);
            }
        }
    }
}
