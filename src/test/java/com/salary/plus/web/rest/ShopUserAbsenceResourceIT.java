package com.salary.plus.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shiver.me.timbers.data.random.RandomLongs.somePositiveLong;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salary.plus.IntegrationTest;
import com.salary.plus.domain.Shop;
import com.salary.plus.domain.ShopBaseSalary;
import com.salary.plus.domain.ShopOrder;
import com.salary.plus.domain.ShopOrderDetail;
import com.salary.plus.domain.ShopPenalty;
import com.salary.plus.domain.ShopSalesItem;
import com.salary.plus.domain.ShopUserBaseSalaryMapping;
import com.salary.plus.domain.ShopUserDailySalary;
import com.salary.plus.domain.ShopUserMapping;
import com.salary.plus.domain.ShopUserPenaltyMapping;
import com.salary.plus.domain.ShopUserSalesSalary;
import com.salary.plus.domain.User;
import com.salary.plus.enums.PenaltyType;
import com.salary.plus.enums.ShopType;
import com.salary.plus.repository.ShopBaseSalaryRepository;
import com.salary.plus.repository.ShopOrderDetailRepository;
import com.salary.plus.repository.ShopOrderRepository;
import com.salary.plus.repository.ShopPenaltyRepository;
import com.salary.plus.repository.ShopRepository;
import com.salary.plus.repository.ShopSalesItemRepository;
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
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class ShopUserAbsenceResourceIT {

    private static final String TARGET_URL = "/api/shops/{shopId}/dates/{date}/users/{userId}/absence";

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

    @Mock
    private DateProvider dateProvider;

    @Autowired
    private MockMvc restUserMockMvc;

    @Nested
    class WhenSuccess {

        private Long shopId;
        private String date;

        private Integer baseSalaryPrice = 500;
        private Long userId;
        private Long shopPenaltyId;
        private Long orderId;

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

        private void mockOrderData(String orderDate, Integer longTimeBarFinePrice, Integer ladyDrinkPrice, Integer guestDrinkPrice) {
            final Integer totalPrice = longTimeBarFinePrice + ladyDrinkPrice + guestDrinkPrice;

            ShopOrder shopOrder = new ShopOrder();
            shopOrder.setShopId(shopId);
            shopOrder.setShopTableId(somePositiveLong());
            shopOrder.setPaid(false);
            shopOrder.setDate(orderDate);
            shopOrder.setTotalPrice(totalPrice);
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
        }

        private ShopPenalty mockPenaltyData(PenaltyType penaltyType, String penaltyTypeValue, int price) {
            ShopPenalty shopPenalty = new ShopPenalty();
            shopPenalty.setShopId(shopId);
            shopPenalty.setType(penaltyType);
            shopPenalty.setTypeValue(penaltyTypeValue);
            shopPenalty.setPrice(price);
            shopPenalty.setNameKo("test ko");
            shopPenalty.setNameEn("test en");

            return shopPenaltyRepository.saveAndFlush(shopPenalty);
        }

        private void mockLateData(Long shopPenaltyId, Integer price) {
            ShopUserPenaltyMapping shopUserPenaltyMapping = new ShopUserPenaltyMapping();
            shopUserPenaltyMapping.setShopPenaltyId(shopPenaltyId);
            shopUserPenaltyMapping.setUserId(userId);
            shopUserPenaltyMapping.setDate(date);
            shopUserPenaltyMapping.setPrice(price);
            shopUserPenaltyMappingRepository.saveAndFlush(shopUserPenaltyMapping);
        }

        @DisplayName("무단결근의 경우")
        @Nested
        class WhenAbsence {

            @DisplayName("이전일에 바파인이 없는 경우")
            @Nested
            class WhenYesterdayHaveNotBarFine {

                private final Integer penaltyPrice = 500;

                @BeforeEach
                void setUp() {
                    mockData();
                    mockPenaltyData(PenaltyType.MANDATORY, "FRI,SAT,SUN", 1000);
                    final ShopPenalty shopPenalty = mockPenaltyData(PenaltyType.DAY, "MON,TUE,WED,THU", penaltyPrice);
                    mockPenaltyData(PenaltyType.TIME, "1H", 100);

                    shopPenaltyId = shopPenalty.getId();

                    given(dateProvider.getWeekday()).willReturn("MON");
                }

                @DisplayName("사용자에 당일 셀러리는 삭제되고 벌금 정보만 정상 등록된다.")
                @Test
                @Transactional
                void create() throws Exception {
                    restUserMockMvc
                        .perform(post(TARGET_URL, shopId, date, userId).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());

                    //결근 정보는 존재한다
                    final ShopUserPenaltyMapping databaseShopUserPenaltyMapping = shopUserPenaltyMappingRepository
                        .findByShopPenaltyIdAndUserIdAndDate(shopPenaltyId, userId, date)
                        .orElseThrow();
                    assertThat(databaseShopUserPenaltyMapping.getPrice()).isEqualTo(penaltyPrice);
                    //사용자 셀러리는 없다
                    final List<ShopUserDailySalary> databaseUserDailySalaries =
                        shopUserDailySalaryRepository.findAllByShopIdAndDateAndUserId(shopId, date, userId);
                    assertThat(databaseUserDailySalaries.size()).isEqualTo(0);
                }
            }

            @DisplayName("이전일에 바파인과 LD 커미션이 있는 경우")
            @Nested
            class WhenYesterdayHasBarFine {

                private final Integer penaltyPrice = 500;
                private final Integer longTimeBarFinePrice = 5000;
                private final Integer ladyDrinkPrice = 350;
                private final Integer guestDrinkPrice = 150;
                private String yesterday;

                @BeforeEach
                void setUp() {
                    mockData();
                    final String yyyyMMdd = DateUtils.getFormatted(DateTimeFormatUtil.yyyy_MM_dd());
                    yesterday = DateUtils.getMinusDay(yyyyMMdd, 1);
                    mockOrderData(yesterday, longTimeBarFinePrice, ladyDrinkPrice, guestDrinkPrice);
                    final ShopPenalty shopPenalty = mockPenaltyData(PenaltyType.BAR_SHARE, "COMMISSION", penaltyPrice);

                    shopPenaltyId = shopPenalty.getId();
                }

                @DisplayName("사용자에 벌금 정보에 커미션 금액이 합산되고 셀러리는 activated false 로 등록된다.")
                @Test
                @Transactional
                void create() throws Exception {
                    restUserMockMvc
                        .perform(post(TARGET_URL, shopId, date, userId).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());

                    //결근 정보는 존재한다
                    final ShopUserPenaltyMapping databaseShopUserPenaltyMapping = shopUserPenaltyMappingRepository
                        .findByShopPenaltyIdAndUserIdAndDate(shopPenaltyId, userId, date)
                        .orElseThrow();
                    assertThat(databaseShopUserPenaltyMapping.getPrice()).isEqualTo(longTimeBarFinePrice + ladyDrinkPrice);
                    //사용자 셀러리는 없다
                    final List<ShopUserDailySalary> databaseUserDailySalaries =
                        shopUserDailySalaryRepository.findAllByShopIdAndDateAndUserId(shopId, date, userId);
                    assertThat(databaseUserDailySalaries.size()).isEqualTo(0);

                    //어제 날자의 커미션 데이터 확인
                    final List<ShopUserSalesSalary> databaseUserSalesSalaries =
                        shopUserSalesSalaryRepository.findAllByShopIdAndDateAndUserId(shopId, yesterday, userId);
                    assertThat(databaseUserSalesSalaries.size()).isEqualTo(2);
                    assertThat(databaseUserSalesSalaries.get(0).isActivated()).isEqualTo(false);
                    assertThat(databaseUserSalesSalaries.get(1).isActivated()).isEqualTo(false);
                }
            }

            @DisplayName("이미 출근 정보가 있는 경우")
            @Nested
            class WhenAlreadyCheckInAndHasLatePenalty {

                private final Integer penaltyPrice = 500;

                @BeforeEach
                void setUp() {
                    mockData();
                    final ShopPenalty shopPenalty = mockPenaltyData(PenaltyType.TIME, "1H", penaltyPrice);

                    shopPenaltyId = shopPenalty.getId();
                    mockLateData(shopPenaltyId, 123);
                }

                @DisplayName("사용자에 출근 정보는 삭제되고 결근 벌금 정보만 등록된다.")
                @Test
                @Transactional
                void create() throws Exception {
                    restUserMockMvc
                        .perform(post(TARGET_URL, shopId, date, userId).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());

                    //결근 정보는 존재한다
                    final ShopUserPenaltyMapping databaseShopUserPenaltyMapping = shopUserPenaltyMappingRepository
                        .findByShopPenaltyIdAndUserIdAndDate(shopPenaltyId, userId, date)
                        .orElseThrow();
                    assertThat(databaseShopUserPenaltyMapping.getPrice()).isEqualTo(penaltyPrice);
                    //사용자 셀러리는 없다
                    final List<ShopUserDailySalary> databaseUserDailySalaries =
                        shopUserDailySalaryRepository.findAllByShopIdAndDateAndUserId(shopId, date, userId);
                    assertThat(databaseUserDailySalaries.size()).isEqualTo(0);

                    //커미션 데이터 확인
                    final List<ShopUserSalesSalary> databaseUserSalesSalaries =
                        shopUserSalesSalaryRepository.findAllByShopIdAndDateAndUserId(shopId, date, userId);
                    assertThat(databaseUserSalesSalaries.size()).isEqualTo(0);
                }
            }
        }
    }
}
