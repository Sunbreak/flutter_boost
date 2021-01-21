// Autogenerated from Pigeon (v0.1.17), do not edit directly.
// See also: https://pub.dev/packages/pigeon
#import "messages.h"
#import <Flutter/Flutter.h>

#if !__has_feature(objc_arc)
#error File requires ARC to be enabled.
#endif

static NSDictionary* wrapResult(NSDictionary *result, FlutterError *error) {
  NSDictionary *errorDict = (NSDictionary *)[NSNull null];
  if (error) {
    errorDict = [NSDictionary dictionaryWithObjectsAndKeys:
        (error.code ? error.code : [NSNull null]), @"code",
        (error.message ? error.message : [NSNull null]), @"message",
        (error.details ? error.details : [NSNull null]), @"details",
        nil];
  }
  return [NSDictionary dictionaryWithObjectsAndKeys:
      (result ? result : [NSNull null]), @"result",
      errorDict, @"error",
      nil];
}

@interface FBCommonParams ()
+(FBCommonParams*)fromMap:(NSDictionary*)dict;
-(NSDictionary*)toMap;
@end

@implementation FBCommonParams
+(FBCommonParams*)fromMap:(NSDictionary*)dict {
  FBCommonParams* result = [[FBCommonParams alloc] init];
  result.pageName = dict[@"pageName"];
  if ((NSNull *)result.pageName == [NSNull null]) {
    result.pageName = nil;
  }
  result.uniqueId = dict[@"uniqueId"];
  if ((NSNull *)result.uniqueId == [NSNull null]) {
    result.uniqueId = nil;
  }
  result.hint = dict[@"hint"];
  if ((NSNull *)result.hint == [NSNull null]) {
    result.hint = nil;
  }
  result.arguments = dict[@"arguments"];
  if ((NSNull *)result.arguments == [NSNull null]) {
    result.arguments = nil;
  }
  return result;
}
-(NSDictionary*)toMap {
  return [NSDictionary dictionaryWithObjectsAndKeys:(self.pageName ? self.pageName : [NSNull null]), @"pageName", (self.uniqueId ? self.uniqueId : [NSNull null]), @"uniqueId", (self.hint ? self.hint : [NSNull null]), @"hint", (self.arguments ? self.arguments : [NSNull null]), @"arguments", nil];
}
@end

@interface FBFlutterRouterApi ()
@property (nonatomic, strong) NSObject<FlutterBinaryMessenger>* binaryMessenger;
@end

@implementation FBFlutterRouterApi
- (instancetype)initWithBinaryMessenger:(NSObject<FlutterBinaryMessenger>*)binaryMessenger {
  self = [super init];
  if (self) {
    self.binaryMessenger = binaryMessenger;
  }
  return self;
}

- (void)pushRoute:(FBCommonParams*)input completion:(void(^)(NSError* _Nullable))completion {
  FlutterBasicMessageChannel *channel =
    [FlutterBasicMessageChannel
      messageChannelWithName:@"dev.flutter.pigeon.FlutterRouterApi.pushRoute"
      binaryMessenger:self.binaryMessenger];
  NSDictionary* inputMap = [input toMap];
  [channel sendMessage:inputMap reply:^(id reply) {
    completion(nil);
  }];
}
- (void)popRoute:(FBCommonParams*)input completion:(void(^)(NSError* _Nullable))completion {
  FlutterBasicMessageChannel *channel =
    [FlutterBasicMessageChannel
      messageChannelWithName:@"dev.flutter.pigeon.FlutterRouterApi.popRoute"
      binaryMessenger:self.binaryMessenger];
  NSDictionary* inputMap = [input toMap];
  [channel sendMessage:inputMap reply:^(id reply) {
    completion(nil);
  }];
}
- (void)onForeground:(FBCommonParams*)input completion:(void(^)(NSError* _Nullable))completion {
  FlutterBasicMessageChannel *channel =
    [FlutterBasicMessageChannel
      messageChannelWithName:@"dev.flutter.pigeon.FlutterRouterApi.onForeground"
      binaryMessenger:self.binaryMessenger];
  NSDictionary* inputMap = [input toMap];
  [channel sendMessage:inputMap reply:^(id reply) {
    completion(nil);
  }];
}
- (void)onBackground:(FBCommonParams*)input completion:(void(^)(NSError* _Nullable))completion {
  FlutterBasicMessageChannel *channel =
    [FlutterBasicMessageChannel
      messageChannelWithName:@"dev.flutter.pigeon.FlutterRouterApi.onBackground"
      binaryMessenger:self.binaryMessenger];
  NSDictionary* inputMap = [input toMap];
  [channel sendMessage:inputMap reply:^(id reply) {
    completion(nil);
  }];
}
- (void)onAppear:(FBCommonParams*)input completion:(void(^)(NSError* _Nullable))completion {
  FlutterBasicMessageChannel *channel =
    [FlutterBasicMessageChannel
      messageChannelWithName:@"dev.flutter.pigeon.FlutterRouterApi.onAppear"
      binaryMessenger:self.binaryMessenger];
  NSDictionary* inputMap = [input toMap];
  [channel sendMessage:inputMap reply:^(id reply) {
    completion(nil);
  }];
}
- (void)onDisappear:(FBCommonParams*)input completion:(void(^)(NSError* _Nullable))completion {
  FlutterBasicMessageChannel *channel =
    [FlutterBasicMessageChannel
      messageChannelWithName:@"dev.flutter.pigeon.FlutterRouterApi.onDisappear"
      binaryMessenger:self.binaryMessenger];
  NSDictionary* inputMap = [input toMap];
  [channel sendMessage:inputMap reply:^(id reply) {
    completion(nil);
  }];
}
@end
void FBNativeRouterApiSetup(id<FlutterBinaryMessenger> binaryMessenger, id<FBNativeRouterApi> api) {
  {
    FlutterBasicMessageChannel *channel =
      [FlutterBasicMessageChannel
        messageChannelWithName:@"dev.flutter.pigeon.NativeRouterApi.pushNativeRoute"
        binaryMessenger:binaryMessenger];
    if (api) {
      [channel setMessageHandler:^(id _Nullable message, FlutterReply callback) {
        FlutterError *error;
        FBCommonParams *input = [FBCommonParams fromMap:message];
        [api pushNativeRoute:input error:&error];
        callback(wrapResult(nil, error));
      }];
    }
    else {
      [channel setMessageHandler:nil];
    }
  }
  {
    FlutterBasicMessageChannel *channel =
      [FlutterBasicMessageChannel
        messageChannelWithName:@"dev.flutter.pigeon.NativeRouterApi.pushFlutterRoute"
        binaryMessenger:binaryMessenger];
    if (api) {
      [channel setMessageHandler:^(id _Nullable message, FlutterReply callback) {
        FlutterError *error;
        FBCommonParams *input = [FBCommonParams fromMap:message];
        [api pushFlutterRoute:input error:&error];
        callback(wrapResult(nil, error));
      }];
    }
    else {
      [channel setMessageHandler:nil];
    }
  }
  {
    FlutterBasicMessageChannel *channel =
      [FlutterBasicMessageChannel
        messageChannelWithName:@"dev.flutter.pigeon.NativeRouterApi.popRoute"
        binaryMessenger:binaryMessenger];
    if (api) {
      [channel setMessageHandler:^(id _Nullable message, FlutterReply callback) {
        FlutterError *error;
        FBCommonParams *input = [FBCommonParams fromMap:message];
        [api popRoute:input error:&error];
        callback(wrapResult(nil, error));
      }];
    }
    else {
      [channel setMessageHandler:nil];
    }
  }
}
